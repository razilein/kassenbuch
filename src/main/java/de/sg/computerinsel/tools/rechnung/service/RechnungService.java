package de.sg.computerinsel.tools.rechnung.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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
        final String nummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nummer");
        final String reparaturnummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "reparaturnummer");
        final String kundennummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "kundenummer");
        final String ersteller = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "ersteller");
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtBezahlt = BooleanUtils.toBoolean(conditions.get("bezahlt"));
        final String art = SearchQueryUtils.getAndRemoveJoker(conditions, "art");
        final String posten = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "posten");

        final boolean mitAngebot = BooleanUtils.toBoolean(conditions.get("mitangebot"));
        final boolean mitBestellung = BooleanUtils.toBoolean(conditions.get("mitbestellung"));
        final boolean mitReparatur = BooleanUtils.toBoolean(conditions.get("mitreparatur"));

        final boolean vorlage = BooleanUtils.toBoolean(conditions.get("vorlage"));

        if (StringUtils.isNumeric(kundeId)) {
            return rechnungViewRepository.findByKundeIdAndVorlage(Ints.tryParse(kundeId), vorlage, pagination);
        } else if (StringUtils.isBlank(nummer) && StringUtils.isBlank(reparaturnummer) && StringUtils.isBlank(ersteller)
                && StringUtils.isBlank(kundennummer) && !istNichtBezahlt && StringUtils.isBlank(posten) && !StringUtils.isNumeric(art)
                && !mitAngebot && !mitBestellung && !mitReparatur) {
            return rechnungViewRepository.findByVorlage(vorlage, pagination);
        } else if (StringUtils.isNotBlank(posten)) {
            return rechnungViewRepository.findByPostenBezeichnungLikeOrPostenSeriennummerLikeOrPostenHinweisLikeAndVorlage(posten, posten,
                    posten, vorlage, pagination);
        } else if (mitAngebot || mitBestellung || mitReparatur) {
            final FindAllByConditionsExecuter<RechnungView> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(rechnungViewRepository, pagination,
                    buildMethodnameForQueryRechnungen(mitAngebot, mitBestellung, mitReparatur), mitAngebot ? mitAngebot : null,
                    mitBestellung ? mitBestellung : null, mitReparatur ? mitReparatur : null, vorlage);
        } else {
            final FindAllByConditionsExecuter<RechnungView> executer = new FindAllByConditionsExecuter<>();
            final Integer zahlart = StringUtils.isNumeric(art) ? Ints.tryParse(art) : null;
            return executer.findByParams(rechnungViewRepository, pagination,
                    buildMethodnameForQueryRechnungen(nummer, reparaturnummer, kundennummer, ersteller, istNichtBezahlt, art), nummer,
                    reparaturnummer, kundennummer, ersteller, istNichtBezahlt ? Boolean.FALSE : null, zahlart, vorlage);
        }
    }

    private String buildMethodnameForQueryRechnungen(final String rechnungNr, final String reparaturNr, final String kundeNr,
            final String ersteller, final boolean istNichtBezahlt, final String art) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(rechnungNr)) {
            methodName += "RechnungNrLikeAnd";
        }
        if (StringUtils.isNotBlank(reparaturNr)) {
            methodName += "ReparaturNrLikeAnd";
        }
        if (StringUtils.isNotBlank(kundeNr)) {
            methodName += "KundeNrLikeAnd";
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
        methodName += "VorlageAnd";
        return StringUtils.removeEnd(methodName, "And");
    }

    private String buildMethodnameForQueryRechnungen(final boolean mitAngebot, final boolean mitBestellung, final boolean mitReparatur) {
        String methodName = "findBy";
        if (mitAngebot) {
            methodName += "MitAngebotAnd";
        }
        if (mitBestellung) {
            methodName += "MitBestellungAnd";
        }
        if (mitReparatur) {
            methodName += "MitReparaturAnd";
        }
        methodName += "VorlageAnd";
        return StringUtils.removeEnd(methodName, "And");
    }

    public Page<Rechnungsposten> listRechnungsposten(final PageRequest pagination, final Integer rechnungId) {
        return rechnungspostenRepository.findAllByRechnungIdOrderByPositionAsc(rechnungId, pagination);
    }

    public RechnungDTO getRechnung(final Integer id, final boolean stornoBeachten) {
        final Optional<Rechnung> rechnung = rechnungRepository.findById(id);
        if (rechnung.isPresent()) {
            final List<Rechnungsposten> posten = stornoBeachten ? listRechnungspostenByRechnungIdOhneStorno(id)
                    : listRechnungspostenByRechnungId(id);
            return new RechnungDTO(rechnung.get(), posten, stornoBeachten, false);
        } else {
            return new RechnungDTO(einstellungenService.getMwstProzent());
        }
    }

    public RechnungDTO getRechnung(final Integer id) {
        return getRechnung(id, false);
    }

    public List<Rechnungsposten> listRechnungspostenByRechnungIdOhneStorno(final Integer id) {
        return rechnungspostenRepository.findAllByRechnungIdAndStornoOrderByPositionAsc(id, false);
    }

    public List<Rechnungsposten> listRechnungspostenByRechnungId(final Integer id) {
        return rechnungspostenRepository.findAllByRechnungIdOrderByPositionAsc(id);
    }

    @Transactional
    public Rechnung saveRechnung(final Rechnung rechnung) {
        if (rechnung.getId() == null) {
            rechnungsdatenHinterlegen(rechnung, LocalDate.now());
            final String nummer = getRechnungsdatumJahrZweistellig(rechnung.getDatum())
                    + mitarbeiterService.getAndSaveNextRechnungsnummer();
            rechnung.setNummer(Ints.tryParse(nummer));
        }
        // Bei Überweisungen oder Paypal muss der Name auf der Rechnung stehen
        if (rechnung.getArt() == Zahlart.UEBERWEISUNG.getCode() || rechnung.getArt() == Zahlart.PAYPAL.getCode()) {
            rechnung.setNameDrucken(true);
        }
        return rechnungRepository.save(rechnung);
    }

    @Transactional
    public Rechnung saveRechnungsvorlage(final Rechnung rechnung) {
        rechnung.setVorlage(true);
        rechnungsdatenHinterlegen(rechnung, LocalDate.of(1990, Month.NOVEMBER, 19));
        // Bei Überweisungen oder Paypal muss der Name auf der Rechnung stehen
        if (rechnung.getArt() == Zahlart.UEBERWEISUNG.getCode() || rechnung.getArt() == Zahlart.PAYPAL.getCode()) {
            rechnung.setNameDrucken(true);
        }
        return rechnungRepository.save(rechnung);
    }

    private void rechnungsdatenHinterlegen(final Rechnung rechnung, final LocalDate datum) {
        rechnung.setErsteller(
                StringUtils.abbreviate(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname(), Rechnung.MAX_LENGTH_MITARBEITER));
        final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
        if (optional.isPresent()) {
            rechnung.setFiliale(optional.get().getFiliale());
        }
        rechnung.setDatum(datum);
        rechnung.setErstelltAm(LocalDateTime.now());
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

    public List<Rechnung> hatKundeOffeneRechnungen(final Integer kundeId) {
        return rechnungRepository.findByKundeIdAndDatumLessThanAndBezahltAndVorlage(kundeId, LocalDate.now(), false, false);
    }

    public void deletePosten(final Rechnung rechnung) {
        rechnungspostenRepository.deleteByRechnungId(rechnung.getId());
    }

}
