package de.sg.computerinsel.tools.rechnung.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.kunde.model.KundeDuplikatDto;
import de.sg.computerinsel.tools.rechnung.dao.RechnungRepository;
import de.sg.computerinsel.tools.rechnung.dao.RechnungViewRepository;
import de.sg.computerinsel.tools.rechnung.dao.RechnungspostenRepository;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.RechnungView;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RechnungService {

    private static final int LAENGE_RECHNUNGSNUMMER_JAHR = 2;

    private final EinstellungenService einstellungenService;

    private final MitarbeiterService mitarbeiterService;

    private final RechnungRepository rechnungRepository;

    private final RechnungViewRepository rechnungViewRepository;

    private final RechnungspostenRepository rechnungspostenRepository;

    public Page<RechnungView> listRechnungen(final PageRequest pagination, final Map<String, String> conditions) {
        final String nummer = SearchQueryUtils.getAndRemoveJoker(conditions, "nummer");
        final String reparaturnummer = SearchQueryUtils.getAndRemoveJoker(conditions, "reparaturnummer");
        final String kundennummer = SearchQueryUtils.getAndRemoveJoker(conditions, "kundenummer");
        final String ersteller = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "ersteller");
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtBezahlt = BooleanUtils.toBoolean(conditions.get("bezahlt"));
        final String art = SearchQueryUtils.getAndRemoveJoker(conditions, "art");
        final String posten = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "posten");

        if (StringUtils.isNumeric(kundeId)) {
            return rechnungViewRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (!StringUtils.isNumeric(nummer) && StringUtils.isBlank(reparaturnummer) && StringUtils.isBlank(ersteller)
                && !StringUtils.isNumeric(kundennummer) && !istNichtBezahlt && StringUtils.isBlank(posten) && !StringUtils.isNumeric(art)) {
            return rechnungViewRepository.findAll(pagination);
        } else if (StringUtils.isNotBlank(posten)) {
            return rechnungViewRepository.findByPostenBezeichnungLikeOrPostenSeriennummerLikeOrPostenHinweisLike(posten, posten, posten,
                    pagination);
        } else if (!istNichtBezahlt) {
            final FindAllByConditionsExecuter<RechnungView> executer = new FindAllByConditionsExecuter<>();
            final Integer zahlart = StringUtils.isNumeric(art) ? Ints.tryParse(art) : null;
            return executer.findByParams(rechnungViewRepository, pagination,
                    buildMethodnameForQueryRechnungen(nummer, reparaturnummer, kundennummer, ersteller, false, art),
                    StringUtils.isNumeric(nummer) ? Ints.tryParse(nummer) : null, reparaturnummer,
                    StringUtils.isNumeric(kundennummer) ? Ints.tryParse(kundennummer) : null, ersteller, zahlart);
        } else {
            final FindAllByConditionsExecuter<RechnungView> executer = new FindAllByConditionsExecuter<>();
            final Integer zahlart = StringUtils.isNumeric(art) ? Ints.tryParse(art) : null;
            return executer.findByParams(rechnungViewRepository, pagination,
                    buildMethodnameForQueryRechnungen(nummer, reparaturnummer, kundennummer, ersteller, istNichtBezahlt, art),
                    StringUtils.isNumeric(nummer) ? Ints.tryParse(nummer) : null, reparaturnummer,
                    StringUtils.isNumeric(kundennummer) ? Ints.tryParse(kundennummer) : null, ersteller, !istNichtBezahlt, zahlart);
        }
    }

    private String buildMethodnameForQueryRechnungen(final String nummer, final String reparaturnummer, final String kundennummer,
            final String ersteller, final boolean istNichtBezahlt, final String art) {
        String methodName = "findBy";
        if (StringUtils.isNumeric(nummer)) {
            methodName += "NummerAnd";
        }
        if (StringUtils.isNotBlank(reparaturnummer)) {
            methodName += "ReparaturNummerAnd";
        }
        if (StringUtils.isNumeric(kundennummer)) {
            methodName += "KundeNummerAnd";
        }
        if (StringUtils.isNotBlank(ersteller)) {
            methodName += "ErstellerLikeAnd";
        }
        if (istNichtBezahlt) {
            methodName += "BezahltAnd";
        }
        if (StringUtils.isNumeric(art)) {
            methodName += "ArtAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public Page<Rechnungsposten> listRechnungsposten(final PageRequest pagination, final Integer rechnungId) {
        return rechnungspostenRepository.findAllByRechnungIdOrderByPositionAsc(rechnungId, pagination);
    }

    public RechnungDTO getRechnung(final Integer id) {
        final Optional<Rechnung> rechnung = rechnungRepository.findById(id);
        if (rechnung.isPresent()) {
            return new RechnungDTO(rechnung.get(), listRechnungspostenByRechnungId(id));
        } else {
            return new RechnungDTO();
        }
    }

    public List<Rechnungsposten> listRechnungspostenByRechnungId(final Integer id) {
        return rechnungspostenRepository.findAllByRechnungIdOrderByPositionAsc(id);
    }

    @Transactional
    public Rechnung saveRechnung(final Rechnung rechnung) {
        if (rechnung.getId() == null) {
            rechnung.setErsteller(StringUtils.abbreviate(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname(),
                    Rechnung.MAX_LENGTH_MITARBEITER));
            final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
            if (optional.isPresent()) {
                rechnung.setFiliale(optional.get().getFiliale());
            }
            rechnung.setDatum(LocalDate.now());
            rechnung.setErstelltAm(LocalDateTime.now());
            final String nummer = getRechnungsdatumJahrZweistellig(rechnung.getDatum())
                    + einstellungenService.getAndSaveNextRechnungsnummer();
            rechnung.setNummer(Ints.tryParse(nummer));
        }
        // Bei Überweisungen oder Paypal muss der Name auf der Rechnung stehen
        if (rechnung.getArt() == Zahlart.UEBERWEISUNG.getCode() || rechnung.getArt() == Zahlart.PAYPAL.getCode()) {
            rechnung.setNameDrucken(true);
        }
        return rechnungRepository.save(rechnung);
    }

    private String getRechnungsdatumJahrZweistellig(final LocalDate rechnungsdatum) {
        return StringUtils.right(String.valueOf(rechnungsdatum.getYear()), LAENGE_RECHNUNGSNUMMER_JAHR);
    }

    public void savePosten(final Rechnungsposten posten) {
        rechnungspostenRepository.save(posten);
    }

    public void deleteRechnung(final int id) {
        rechnungRepository.deleteById(id);
    }

    public List<DefaultKeyValue<Integer, String>> getZahlarten() {
        return Arrays.asList(Zahlart.values()).stream().map(z -> new DefaultKeyValue<>(z.getCode(), z.getBezeichnung()))
                .collect(Collectors.toList());
    }

    public List<Rechnung> listBarRechnungenByDatum(final LocalDate datum) {
        return rechnungRepository.findAllByDatumAndArtOrderByNummerAsc(datum, Zahlart.BAR.getCode());
    }

    public Rechnung rechnungBezahlt(final Rechnung rechnung, final boolean bezahlt) {
        rechnung.setBezahlt(bezahlt);
        return rechnungRepository.save(rechnung);
    }

    public void duplikateZusammenfuehren(final KundeDuplikatDto dto) {
        rechnungRepository.findByKundeId(dto.getDuplikat().getId()).forEach(rechnung -> {
            rechnung.setKunde(dto.getKunde());
            rechnungRepository.save(rechnung);
        });
    }

}
