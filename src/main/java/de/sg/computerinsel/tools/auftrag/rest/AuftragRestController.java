package de.sg.computerinsel.tools.auftrag.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.AUFTRAG;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.angebot.dto.AngebotDto;
import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.angebot.service.AngebotService;
import de.sg.computerinsel.tools.auftrag.model.Auftrag;
import de.sg.computerinsel.tools.auftrag.service.AuftragService;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.service.FeiertagUtils;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;

@RestController
@RequestMapping("/auftrag")
public class AuftragRestController {

    @Autowired
    private AngebotService angebotService;

    @Autowired
    private KundeService kundeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private AuftragService service;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<Auftrag> list(@RequestBody final SearchData data) {
        return service.listAuftraege(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/{id}")
    public Auftrag getAuftrag(@PathVariable final Integer id) {
        final Optional<Auftrag> optional = service.getAuftrag(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), AUFTRAG, optional.get().getKunde().getNameKomplett(), ANGESEHEN);
        }
        return optional.orElseGet(this::createAuftrag);
    }

    private Auftrag createAuftrag() {
        final Auftrag auftrag = new Auftrag();
        auftrag.setAngebot(new Angebot());
        auftrag.setErsteller(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
        auftrag.setKunde(new Kunde());
        auftrag.setDatum(berechneAbholdatum());
        auftrag.setAnzahlung("Keine");
        return auftrag;
    }

    @GetMapping("/datum")
    public Map<String, Object> getAbholdatumUndZeit() {
        return Collections.singletonMap("datum", berechneAbholdatum());
    }

    private LocalDate berechneAbholdatum() {
        LocalDate date = LocalDate.now();
        date = DateUtils.plusWorkdays(date, 3);
        final List<LocalDate> notAllowedDays = service.listDaysWithMin5AbholungenAndAuftragNotErledigt();
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || FeiertagUtils.isFeiertag(date)
                || notAllowedDays.stream().anyMatch(dateIsEqual(date))) {
            date = date.plusDays(1);
        }
        return date;
    }

    private Predicate<? super LocalDate> dateIsEqual(final LocalDate date) {
        return d -> d.equals(date);
    }

    @PutMapping
    public Map<String, Object> saveAuftrag(@RequestBody final Auftrag auftrag) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(auftrag));
        if (result.isEmpty()) {
            final boolean isErstellen = auftrag.getId() == null;
            if (auftrag.getAngebot() != null && auftrag.getAngebot().getId() == null) {
                auftrag.setAngebot(null);
            }
            if (auftrag.getKunde() != null && auftrag.getKunde().getId() == null) {
                auftrag.setKunde(null);
            }
            if (isErstellen) {
                auftrag.setErstelltAm(LocalDateTime.now());
                auftrag.setErsteller(StringUtils.abbreviate(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname(),
                        Reparatur.MAX_LENGTH_MITARBEITER));
                final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
                if (optional.isPresent()) {
                    auftrag.setFiliale(optional.get().getFiliale());
                }
            }
            final Auftrag saved = service.save(auftrag);
            angebotErledigen(saved);
            if (isErstellen && auftrag.getKunde() != null && !auftrag.getKunde().isDsgvo()) {
                kundeService.saveDsgvo(auftrag.getKunde().getId());
                result.put(Message.INFO.getCode(), messageService.get("dsgvo.info"));
            } else {
                result.put(Message.SUCCESS.getCode(), messageService.get("auftrag.save.success", auftrag.getId()));
            }
            result.put("auftrag", saved);
            protokollService.write(saved.getId(), AUFTRAG, saved.getKunde().getNameKomplett(), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    private void angebotErledigen(final Auftrag saved) {
        if (saved.getAngebot() != null && saved.getAngebot().getId() != null) {
            final AngebotDto dto = angebotService.getAngebot(saved.getAngebot().getId());
            if (dto.getAngebot().getId() != null) {
                angebotService.angebotErledigen(dto.getAngebot(), true);
            }
        }

    }

    @PutMapping("/erledigen")
    public Map<String, Object> auftragErledigen(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        if (obj.getId() == null) {
            result.put(Message.ERROR.getCode(), messageService.get("auftrag.save.error"));
        } else {
            final Optional<Auftrag> optional = service.getAuftrag(obj.getId());
            if (optional.isPresent()) {
                final Auftrag auftrag = optional.get();
                final boolean erledigt = !auftrag.isErledigt();
                service.auftragErledigen(auftrag, erledigt);
                protokollService.write(auftrag.getId(), AUFTRAG, messageService.get("protokoll.erledigt",
                        auftrag.getKunde().getNameKomplett(), BooleanUtils.toStringYesNo(erledigt)), GEAENDERT);
                result.put(Message.SUCCESS.getCode(), messageService.get("auftrag.save.success", auftrag.getId()));
            }
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> deleteAuftrag(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Auftrag> optional = service.getAuftrag(id);
        service.deleteAuftrag(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), AUFTRAG, optional.get().getKunde().getNameKomplett(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("auftrag.delete.success"));
    }

}
