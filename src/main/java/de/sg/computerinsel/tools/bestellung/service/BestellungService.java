package de.sg.computerinsel.tools.bestellung.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.bestellung.dao.BestellungRepository;
import de.sg.computerinsel.tools.bestellung.dao.VAuftraegeJeTagRepository;
import de.sg.computerinsel.tools.bestellung.dao.VBestellungRepository;
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.bestellung.model.VAuftraegeJeTag;
import de.sg.computerinsel.tools.bestellung.model.VBestellung;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BestellungService {

    private static final int LAENGE_BESTELLNUMMER_JAHR = 2;

    private final BestellungRepository bestellungRepository;

    private final VBestellungRepository vBestellungRepository;

    private final MitarbeiterService mitarbeiterService;

    private final VAuftraegeJeTagRepository vAuftraegeJeTagRepository;

    public List<LocalDate> listDaysWithMin5AbholungenAndAuftragNotErledigt() {
        return vAuftraegeJeTagRepository.findByAnzahlGesamtGreaterThanEqualAndDatumGreaterThanOrderByDatumAsc(5, LocalDate.now()).stream()
                .map(VAuftraegeJeTag::getDatum).collect(Collectors.toList());
    }

    public Optional<Bestellung> getBestellung(final Integer id) {
        return bestellungRepository.findById(id);
    }

    public Bestellung save(final Bestellung bestellung) {
        final boolean isErstellen = bestellung.getId() == null;
        if (isErstellen) {
            bestellung.setErstelltAm(LocalDateTime.now());
            if (StringUtils.isBlank(bestellung.getErsteller())) {
                bestellung.setErsteller(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
            }
            bestellung.setErsteller(StringUtils.abbreviate(bestellung.getErsteller(), Reparatur.MAX_LENGTH_MITARBEITER));
            bestellung.setFiliale(mitarbeiterService.getAngemeldeterMitarbeiterFiliale().orElseGet(Filiale::new));
            final String nummer = getBestellungJahrZweistellig() + mitarbeiterService.getAndSaveNextBestellnummer();
            bestellung.setNummer(Ints.tryParse(nummer));
        }
        return bestellungRepository.save(bestellung);
    }

    private String getBestellungJahrZweistellig() {
        return StringUtils.right(String.valueOf(LocalDate.now().getYear()), LAENGE_BESTELLNUMMER_JAHR);
    }

    public void deleteBestellung(final Integer id) {
        bestellungRepository.deleteById(id);
    }

    public Bestellung bestellungErledigen(final Bestellung besellung, final boolean erledigt) {
        besellung.setErledigt(erledigt);
        besellung.setErledigungsdatum(erledigt ? LocalDateTime.now() : null);
        return save(besellung);
    }

    public Page<VBestellung> listBestellungen(final PageRequest pagination, final Map<String, String> conditions) {
        final String name = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "suchfeld_name");
        final String nummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nummer");
        final String kundennummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "kundennummer");
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtErledigt = BooleanUtils.toBoolean(conditions.get("erledigt"));
        final String beschreibung = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "beschreibung");

        if (StringUtils.isNumeric(kundeId)) {
            return vBestellungRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (StringUtils.isBlank(nummer) && StringUtils.isBlank(name) && StringUtils.isBlank(beschreibung) && !istNichtErledigt
                && StringUtils.isBlank(kundennummer)) {
            return vBestellungRepository.findAll(pagination);
        } else if (istNichtErledigt) {
            final FindAllByConditionsExecuter<VBestellung> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vBestellungRepository, pagination,
                    buildMethodnameForQueryBestellung(name, beschreibung, kundennummer, istNichtErledigt, nummer), name, beschreibung,
                    kundennummer, !istNichtErledigt, nummer);
        } else {
            final FindAllByConditionsExecuter<VBestellung> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vBestellungRepository, pagination,
                    buildMethodnameForQueryBestellung(name, beschreibung, kundennummer, false, nummer), name, beschreibung, kundennummer,
                    nummer);
        }
    }

    private String buildMethodnameForQueryBestellung(final String name, final String beschreibung, final String kundeNr,
            final boolean istNichtErledigt, final String bestellungNr) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(name)) {
            methodName += "KundeSuchfeldNameLikeAnd";
        }
        if (StringUtils.isNotBlank(beschreibung)) {
            methodName += "BeschreibungLikeAnd";
        }
        if (StringUtils.isNotBlank(kundeNr)) {
            methodName += "KundeNrLikeAnd";
        }
        if (istNichtErledigt) {
            methodName += "ErledigtAnd";
        }
        if (StringUtils.isNotBlank(bestellungNr)) {
            methodName += "BestellungNrLikeAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

}
