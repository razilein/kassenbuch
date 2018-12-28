package de.sg.computerinsel.tools.reparatur.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.reparatur.dao.KundeRepository;
import de.sg.computerinsel.tools.reparatur.dao.ReparaturRepository;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.ReparaturArt;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReparaturService {

    private final EinstellungenService einstellungenService;

    private final KundeRepository kundeRepository;

    private final ReparaturRepository reparaturRepository;

    public Page<Kunde> listKunden(final PageRequest pagination, final Map<String, String> conditions) {
        final String nachname = SearchQueryUtils.getAndReplaceJoker(conditions, "nachname");
        final String vorname = SearchQueryUtils.getAndReplaceJoker(conditions, "vorname");
        final String plz = SearchQueryUtils.getAndReplaceJoker(conditions, "plz");

        if (StringUtils.isBlank(nachname) && StringUtils.isBlank(vorname) && StringUtils.isBlank(plz)) {
            return kundeRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<Kunde> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(kundeRepository, pagination, buildMethodnameForQueryKunde(nachname, vorname, plz), nachname,
                    vorname, plz);
        }
    }

    private String buildMethodnameForQueryKunde(final String nachname, final String vorname, final String plz) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "NachnameLikeAnd";
        }
        if (StringUtils.isNotBlank(vorname)) {
            methodName += "VornameLikeAnd";
        }
        if (StringUtils.isNotBlank(plz)) {
            methodName += "PlzLikeAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public Optional<Kunde> getKunde(final Integer id) {
        return kundeRepository.findById(id);
    }

    public void saveDsgvo(final Integer id) {
        getKunde(id).ifPresent(k -> {
            k.setDsgvo(true);
            save(k);
        });
    }

    public Kunde save(final Kunde kunde) {
        if (kunde.getNummer() == null) {
            kunde.setNummer(kundeRepository.getNextNummer());
        }
        return kundeRepository.save(kunde);
    }

    public void deleteKunde(final Integer id) {
        kundeRepository.deleteById(id);
    }

    public Page<Reparatur> listReparaturen(final PageRequest pagination, final Map<String, String> conditions) {
        final String nachname = SearchQueryUtils.getAndReplaceJoker(conditions, "nachname");
        final String nummer = SearchQueryUtils.getAndReplaceJoker(conditions, "nummer");
        final String kundeId = SearchQueryUtils.getAndReplaceJoker(conditions, "kunde.id");

        if (StringUtils.isNumeric(kundeId)) {
            return reparaturRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (StringUtils.isBlank(nachname) && StringUtils.isBlank(nummer)) {
            return reparaturRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<Reparatur> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(reparaturRepository, pagination, buildMethodnameForQueryReparatur(nachname, nummer), nachname,
                    nummer);
        }
    }

    private String buildMethodnameForQueryReparatur(final String nachname, final String nummer) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "KundeNachnameLikeAnd";
        }
        if (StringUtils.isNotBlank(nummer)) {
            methodName += "NummerLikeAnd";
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
            reparatur.setNummer(LocalDate.now().getYear() + einstellungenService.getAndSaveNextReparaturnummer());
        }
        return reparaturRepository.save(reparatur);
    }

    public void deleteReparatur(final Integer id) {
        reparaturRepository.deleteById(id);
    }

}
