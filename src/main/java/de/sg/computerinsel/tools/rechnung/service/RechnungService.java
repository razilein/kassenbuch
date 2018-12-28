package de.sg.computerinsel.tools.rechnung.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.rechnung.dao.RechnungRepository;
import de.sg.computerinsel.tools.rechnung.dao.RechnungspostenRepository;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RechnungService {

    private final EinstellungenService einstellungenService;

    private final MitarbeiterService mitarbeiterService;

    private final RechnungRepository rechnungRepository;

    private final RechnungspostenRepository rechnungspostenRepository;

    public Page<Rechnung> listRechnungen(final PageRequest pagination, final Map<String, String> conditions) {
        final String nummer = SearchQueryUtils.getAndReplaceJoker(conditions, "nummer");
        final String reparaturnummer = SearchQueryUtils.getAndReplaceJoker(conditions, "reparaturnummer");
        final String kundennummer = SearchQueryUtils.getAndReplaceJoker(conditions, "kundenummer");
        final String ersteller = SearchQueryUtils.getAndReplaceJoker(conditions, "ersteller");
        final String kundeId = SearchQueryUtils.getAndReplaceJoker(conditions, "kunde.id");

        if (StringUtils.isNumeric(kundeId)) {
            return rechnungRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (!StringUtils.isNumeric(nummer) && StringUtils.isBlank(reparaturnummer) && StringUtils.isBlank(ersteller)
                && !StringUtils.isNumeric(kundennummer)) {
            return rechnungRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<Rechnung> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(rechnungRepository, pagination,
                    buildMethodnameForQueryRechnungen(nummer, reparaturnummer, kundennummer, ersteller),
                    StringUtils.isNumeric(nummer) ? Ints.tryParse(nummer) : null, reparaturnummer,
                    StringUtils.isNumeric(kundennummer) ? Ints.tryParse(kundennummer) : null, ersteller);
        }
    }

    private String buildMethodnameForQueryRechnungen(final String nummer, final String reparaturnummer, final String kundennummer,
            final String ersteller) {
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
        return StringUtils.removeEnd(methodName, "And");
    }

    public Page<Rechnungsposten> listRechnungsposten(final PageRequest pagination, final Integer rechnungId) {
        return rechnungspostenRepository.findAllByRechnungIdOrderByPositionAsc(rechnungId, pagination);
    }

    public RechnungDTO getRechnung(final Integer id) {
        final Optional<Rechnung> rechnung = rechnungRepository.findById(id);
        if (rechnung.isPresent()) {
            return new RechnungDTO(rechnung.get(), rechnungspostenRepository.findAllByRechnungIdOrderByPositionAsc(id));
        } else {
            return new RechnungDTO();
        }
    }

    @Transactional
    public Rechnung saveRechnung(final Rechnung rechnung) {
        if (rechnung.getId() == null) {
            rechnung.setErsteller(StringUtils.abbreviate(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname(),
                    Rechnung.MAX_LENGTH_MITARBEITER));
            rechnung.setDatum(LocalDate.now());
            final String nummer = String.valueOf(rechnung.getDatum().getYear()) + einstellungenService.getAndSaveNextRechnungsnummer();
            rechnung.setNummer(Ints.tryParse(nummer));
        }
        return rechnungRepository.save(rechnung);
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

}
