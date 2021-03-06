package de.sg.computerinsel.tools.reparatur.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.bestellung.service.BestellungService;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.model.KundeDuplikatDto;
import de.sg.computerinsel.tools.kunde.service.EmailService;
import de.sg.computerinsel.tools.reparatur.dao.ReparaturRepository;
import de.sg.computerinsel.tools.reparatur.dao.VReparaturRepository;
import de.sg.computerinsel.tools.reparatur.model.GeraetepasswortArt;
import de.sg.computerinsel.tools.reparatur.model.PruefstatusGeraet;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.ReparaturArt;
import de.sg.computerinsel.tools.reparatur.model.VReparatur;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReparaturService {

    private static final int LAENGE_REPARATURNUMMER_JAHR = 2;

    private final BestellungService bestellungService;

    private final EmailService emailService;

    private final EinstellungenService einstellungenService;

    private final MitarbeiterService mitarbeiterService;

    private final ReparaturRepository reparaturRepository;

    private final VReparaturRepository vReparaturRepository;

    public Page<VReparatur> listReparaturen(final PageRequest pagination, final Map<String, String> conditions) {
        final String name = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "suchfeld_name");
        final String nummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nummer");
        final String kundennummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "kundennummer");
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtErledigt = BooleanUtils.toBoolean(conditions.get("erledigt"));

        if (StringUtils.isNumeric(kundeId)) {
            return vReparaturRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (StringUtils.isBlank(name) && StringUtils.isBlank(nummer) && !istNichtErledigt && StringUtils.isBlank(kundennummer)) {
            return vReparaturRepository.findAll(pagination);
        } else if (istNichtErledigt) {
            final FindAllByConditionsExecuter<VReparatur> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vReparaturRepository, pagination,
                    buildMethodnameForQueryReparatur(name, nummer, istNichtErledigt, kundennummer), name, nummer, !istNichtErledigt,
                    kundennummer);
        } else {
            final FindAllByConditionsExecuter<VReparatur> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vReparaturRepository, pagination,
                    buildMethodnameForQueryReparatur(name, nummer, false, kundennummer), name, nummer, kundennummer);
        }
    }

    private String buildMethodnameForQueryReparatur(final String nachname, final String reparaturNr, final boolean istNichtErledigt,
            final String kundeNr) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "KundeSuchfeldNameLikeAnd";
        }
        if (StringUtils.isNotBlank(reparaturNr)) {
            methodName += "ReparaturNrLikeAnd";
        }
        if (istNichtErledigt) {
            methodName += "ErledigtAnd";
        }
        if (StringUtils.isNotBlank(kundeNr)) {
            methodName += "KundeNrLikeAnd";
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

    String getReparaturJahrZweistellig() {
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

    public Reparatur createReparatur() {
        final Reparatur reparatur = new Reparatur();
        reparatur.setBestellung(new Bestellung());
        reparatur.setKunde(new Kunde());
        reparatur.setAbholdatum(berechneAbholdatum(false));
        reparatur.setAbholzeit(berechneAbholzeit(false));
        reparatur.setArt(ReparaturArt.REPARATUR.getCode());
        reparatur.setFunktionsfaehig(PruefstatusGeraet.NICHT_DEFINIERT.getCode());
        reparatur.setGeraetErhalten(true);
        return reparatur;
    }

    public LocalDate berechneAbholdatum(final boolean express) {
        LocalDate date = LocalDate.now();
        if (express && LocalTime.now().isAfter(LocalTime.of(13, 30))) {
            date = DateUtils.plusWorkdays(date, 1);
        } else if (!express) {
            date = DateUtils.plusWorkdays(date, 3);
        }
        final List<LocalDate> notAllowedDays = listDaysWithMin5AbholungenAndAuftragNotErledigt();
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || FeiertagUtils.isFeiertag(date)
                || notAllowedDays.stream().anyMatch(dateIsEqual(date))) {
            date = date.plusDays(1);
        }
        return date;
    }

    private Predicate<? super LocalDate> dateIsEqual(final LocalDate date) {
        return d -> d.equals(date);
    }

    public LocalTime berechneAbholzeit(final boolean express) {
        LocalTime time = LocalTime.of(16, 30);
        if (express) {
            if (LocalTime.now().isBefore(LocalTime.of(13, 30))) {
                time = LocalTime.of(18, 30);
            } else {
                time = LocalTime.of(13, 30);
            }
        }
        return time;
    }

    public void geraetErhalten(final Reparatur reparatur) {
        reparatur.setGeraetErhalten(true);
        save(reparatur);
        emailService.sendeEmail(reparatur, einstellungenService.getRoboterEmail().getWert(),
                emailService.getGeraetErhaltenMailText(reparatur));
    }

}
