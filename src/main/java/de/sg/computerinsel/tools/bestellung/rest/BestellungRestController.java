package de.sg.computerinsel.tools.bestellung.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.BESETLLUNG;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.BooleanUtils;
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

import de.sg.computerinsel.tools.angebot.dto.AngebotDto;
import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.angebot.service.AngebotService;
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.bestellung.model.VBestellung;
import de.sg.computerinsel.tools.bestellung.service.BestellungService;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.service.FeiertagUtils;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.model.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;
import de.sg.computerinsel.tools.util.DateUtils;

@RestController
@RequestMapping("/bestellung")
public class BestellungRestController {

    @Autowired
    private AngebotService angebotService;

    @Autowired
    private KundeService kundeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private BestellungService service;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<VBestellung> list(@RequestBody final SearchData data) {
        return service.listBestellungen(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/{id}")
    public Bestellung getBestellung(@PathVariable final Integer id) {
        final Optional<Bestellung> optional = service.getBestellung(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), BESETLLUNG, String.valueOf(optional.get().getNummer()), ANGESEHEN);
        }
        return optional.orElseGet(this::createBestellung);
    }

    private Bestellung createBestellung() {
        final Bestellung bestellung = new Bestellung();
        bestellung.setAngebot(new Angebot());
        bestellung.setKunde(new Kunde());
        bestellung.setDatum(berechneAbholdatum());
        bestellung.setAnzahlung("Keine");
        return bestellung;
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
    public Map<String, Object> saveBestellung(@RequestBody final Bestellung bestellung) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(bestellung));
        if (result.isEmpty()) {
            if (bestellung.getAngebot() != null && bestellung.getAngebot().getId() == null) {
                bestellung.setAngebot(null);
            }
            if (bestellung.getKunde() != null && bestellung.getKunde().getId() == null) {
                bestellung.setKunde(null);
            }
            final Bestellung saved = service.save(bestellung);
            angebotErledigen(saved);
            final boolean isErstellen = bestellung.getId() == null;
            if (isErstellen && bestellung.getKunde() != null && !bestellung.getKunde().isDsgvo()) {
                kundeService.saveDsgvo(bestellung.getKunde().getId());
                result.put(Message.INFO.getCode(), messageService.get("dsgvo.info"));
            } else {
                result.put(Message.SUCCESS.getCode(), messageService.get("bestellung.save.success", bestellung.getNummer()));
            }
            result.put("bestellung", saved);
            protokollService.write(saved.getId(), BESETLLUNG, String.valueOf(saved.getNummer()), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    private void angebotErledigen(final Bestellung saved) {
        if (saved.getAngebot() != null && saved.getAngebot().getId() != null) {
            final AngebotDto dto = angebotService.getAngebot(saved.getAngebot().getId());
            if (dto.getAngebot().getId() != null) {
                angebotService.angebotErledigen(dto.getAngebot(), true);
            }
        }

    }

    @PutMapping("/erledigen")
    public Map<String, Object> bestellungErledigen(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        if (obj.getId() == null) {
            result.put(Message.ERROR.getCode(), messageService.get("bestellung.save.error"));
        } else {
            final Optional<Bestellung> optional = service.getBestellung(obj.getId());
            if (optional.isPresent()) {
                final Bestellung bestellung = optional.get();
                final boolean erledigt = !bestellung.isErledigt();
                service.bestellungErledigen(bestellung, erledigt);
                protokollService.write(bestellung.getId(), BESETLLUNG, messageService.get("protokoll.erledigt",
                        String.valueOf(bestellung.getNummer()), BooleanUtils.toStringYesNo(erledigt)), GEAENDERT);
                result.put(Message.SUCCESS.getCode(), messageService.get("bestellung.save.success", bestellung.getNummer()));
            }
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> deleteBestellung(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Bestellung> optional = service.getBestellung(id);
        service.deleteBestellung(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), BESETLLUNG, String.valueOf(optional.get().getNummer()), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("bestellung.delete.success"));
    }

}
