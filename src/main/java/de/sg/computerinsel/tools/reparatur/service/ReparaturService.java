package de.sg.computerinsel.tools.reparatur.service;

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

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.bestellung.service.BestellungService;
import de.sg.computerinsel.tools.kunde.model.KundeDuplikatDto;
import de.sg.computerinsel.tools.reparatur.dao.ReparaturRepository;
import de.sg.computerinsel.tools.reparatur.model.PruefstatusGeraet;
import de.sg.computerinsel.tools.reparatur.model.GeraetepasswortArt;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.ReparaturArt;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReparaturService {

    private static final int LAENGE_REPARATURNUMMER_JAHR = 2;

    private final BestellungService bestellungService;

    private final MitarbeiterService mitarbeiterService;

    private final ReparaturRepository reparaturRepository;

    public Page<Reparatur> listReparaturen(final PageRequest pagination, final Map<String, String> conditions) {
        final String name = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "suchfeld_name");
        final String nummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nummer");
        String kundennummer = SearchQueryUtils.getAndRemoveJoker(conditions, "kundennummer");
        kundennummer = StringUtils.isNumeric(kundennummer) ? kundennummer : null;
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtErledigt = BooleanUtils.toBoolean(conditions.get("erledigt"));

        if (StringUtils.isNumeric(kundeId)) {
            return reparaturRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (StringUtils.isBlank(name) && StringUtils.isBlank(nummer) && !istNichtErledigt && kundennummer == null) {
            return reparaturRepository.findAll(pagination);
        } else if (istNichtErledigt) {
            final FindAllByConditionsExecuter<Reparatur> executer = new FindAllByConditionsExecuter<>();
            final Integer kdNr = kundennummer == null ? null : Ints.tryParse(kundennummer);
            return executer.findByParams(reparaturRepository, pagination,
                    buildMethodnameForQueryReparatur(name, nummer, istNichtErledigt, kundennummer), name, nummer, !istNichtErledigt, kdNr);
        } else {
            final FindAllByConditionsExecuter<Reparatur> executer = new FindAllByConditionsExecuter<>();
            final Integer kdNr = kundennummer == null ? null : Ints.tryParse(kundennummer);
            return executer.findByParams(reparaturRepository, pagination,
                    buildMethodnameForQueryReparatur(name, nummer, false, kundennummer), name, nummer, kdNr);
        }
    }

    private String buildMethodnameForQueryReparatur(final String nachname, final String nummer, final boolean istNichtErledigt,
            final String kundennummer) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "KundeSuchfeldNameLikeAnd";
        }
        if (StringUtils.isNotBlank(nummer)) {
            methodName += "NummerLikeAnd";
        }
        if (istNichtErledigt) {
            methodName += "ErledigtAnd";
        }
        if (StringUtils.isNotBlank(kundennummer)) {
            methodName += "KundeNummerAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public Optional<Reparatur> getReparatur(final Integer id) {
        return reparaturRepository.findById(id);
    }

    public List<DefaultKeyValue<Integer, String>> getReparaturarten() {
        return Arrays.asList(ReparaturArt.values()).stream().map(r -> new DefaultKeyValue<>(r.getCode(), r.getDescription()))
                .collect(Collectors.toList());
    }

    public Reparatur reparaturErledigen(final Reparatur reparatur, final boolean erledigt) {
        reparatur.setGeraetepasswort(null);
        reparatur.setErledigt(erledigt);
        reparatur.setErledigungsdatum(erledigt ? LocalDateTime.now() : null);
        return save(reparatur);
    }

    public Reparatur save(final Reparatur reparatur) {
        if (StringUtils.isBlank(reparatur.getNummer())) {
            reparatur.setNummer(getReparaturJahrZweistellig() + mitarbeiterService.getAndSaveNextReparaturnummer());
        }
        return reparaturRepository.save(reparatur);
    }

    private String getReparaturJahrZweistellig() {
        return StringUtils.right(String.valueOf(LocalDate.now().getYear()), LAENGE_REPARATURNUMMER_JAHR);
    }

    public void deleteReparatur(final Integer id) {
        reparaturRepository.deleteById(id);
    }

    public List<LocalDate> listDaysWithMin5AbholungenAndAuftragNotErledigt() {
        return bestellungService.listDaysWithMin5AbholungenAndAuftragNotErledigt();
    }

    public void duplikateZusammenfuehren(final KundeDuplikatDto dto) {
        reparaturRepository.findByKundeId(dto.getDuplikat().getId()).forEach(reparatur -> {
            reparatur.setKunde(dto.getKunde());
            reparaturRepository.save(reparatur);
        });
    }

    public List<DefaultKeyValue<Integer, String>> getGeraetepasswortarten() {
        return Arrays.asList(GeraetepasswortArt.values()).stream().map(r -> new DefaultKeyValue<>(r.getCode(), r.getDescription()))
                .collect(Collectors.toList());
    }

    public List<DefaultKeyValue<Integer, String>> getPruefstatus() {
        return Arrays.asList(PruefstatusGeraet.values()).stream().map(r -> new DefaultKeyValue<>(r.getCode(), r.getDescription()))
                .collect(Collectors.toList());
    }

}
