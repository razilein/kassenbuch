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
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.bestellung.model.VAuftraegeJeTag;
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
            bestellung.setErsteller(StringUtils.abbreviate(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname(),
                    Reparatur.MAX_LENGTH_MITARBEITER));
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

    public Page<Bestellung> listBestellungen(final PageRequest pagination, final Map<String, String> conditions) {
        final String name = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "suchfeld_name");
        final String nummer = SearchQueryUtils.getAndRemoveJoker(conditions, "nummer");
        String kundennummer = SearchQueryUtils.getAndRemoveJoker(conditions, "kundennummer");
        kundennummer = StringUtils.isNumeric(kundennummer) ? kundennummer : null;
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtErledigt = BooleanUtils.toBoolean(conditions.get("erledigt"));
        final String beschreibung = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "beschreibung");

        if (StringUtils.isNumeric(kundeId)) {
            return bestellungRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (!StringUtils.isNumeric(nummer) && StringUtils.isBlank(name) && StringUtils.isBlank(beschreibung) && !istNichtErledigt
                && kundennummer == null) {
            return bestellungRepository.findAll(pagination);
        } else if (istNichtErledigt) {
            final FindAllByConditionsExecuter<Bestellung> executer = new FindAllByConditionsExecuter<>();
            final Integer nr = StringUtils.isNumeric(nummer) ? Ints.tryParse(nummer) : null;
            final Integer kdNr = kundennummer == null ? null : Ints.tryParse(kundennummer);
            return executer.findByParams(bestellungRepository, pagination,
                    buildMethodnameForQueryBestellung(name, beschreibung, kundennummer, istNichtErledigt, nummer), name, beschreibung, kdNr,
                    !istNichtErledigt, nr);
        } else {
            final FindAllByConditionsExecuter<Bestellung> executer = new FindAllByConditionsExecuter<>();
            final Integer nr = StringUtils.isNumeric(nummer) ? Ints.tryParse(nummer) : null;
            final Integer kdNr = kundennummer == null ? null : Ints.tryParse(kundennummer);
            return executer.findByParams(bestellungRepository, pagination,
                    buildMethodnameForQueryBestellung(name, beschreibung, kundennummer, false, nummer), name, beschreibung, kdNr, nr);
        }
    }

    private String buildMethodnameForQueryBestellung(final String name, final String beschreibung, final String kundennummer,
            final boolean istNichtErledigt, final String nummer) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(name)) {
            methodName += "KundeSuchfeldNameLikeAnd";
        }
        if (StringUtils.isNotBlank(beschreibung)) {
            methodName += "BeschreibungLikeAnd";
        }
        if (StringUtils.isNotBlank(kundennummer)) {
            methodName += "KundeNummerAnd";
        }
        if (istNichtErledigt) {
            methodName += "ErledigtAnd";
        }
        if (StringUtils.isNumeric(nummer)) {
            methodName += "NummerAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

}
