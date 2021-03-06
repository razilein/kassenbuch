package de.sg.computerinsel.tools.reparatur.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.REPARATUR;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.bestellung.service.BestellungService;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.VReparatur;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;

@RestController
@RequestMapping("/reparatur")
public class ReparaturRestController {

    @Autowired
    private BestellungService bestellungService;

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private KundeService kundeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private ReparaturService service;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<VReparatur> getReparaturen(@RequestBody final SearchData data) {
        if (StringUtils.isBlank(data.getData().getSort())) {
            data.getData().setSort("abholdatum");
            data.getData().setSortorder(Sort.Direction.DESC.name());
        }
        final PageRequest pagination = data.getData().getPagination();
        return service.listReparaturen(pagination, data.getConditions());
    }

    @GetMapping("/{id}")
    public Reparatur getReparatur(@PathVariable final Integer id) {
        final Optional<Reparatur> optional = service.getReparatur(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), REPARATUR, optional.get().getNummer(), ANGESEHEN);
        }
        return optional.orElseGet(service::createReparatur);
    }

    @GetMapping("/abholdatum/{express}")
    public Map<String, Object> getAbholdatumUndZeit(@PathVariable final boolean express) {
        final Map<String, Object> result = new HashMap<>();
        final LocalDate abholdatum = service.berechneAbholdatum(express);
        result.put("abholdatum", abholdatum);

        final LocalTime abholzeit = abholdatum.getDayOfWeek() == DayOfWeek.SATURDAY ? LocalTime.of(12, 0)
                : service.berechneAbholzeit(express);
        result.put("abholzeit", abholzeit.format(DateTimeFormatter.ofPattern("HH:mm")));
        return result;
    }

    @PutMapping
    public Map<String, Object> saveReparatur(@RequestBody final Reparatur reparatur) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(reparatur));
        if (result.isEmpty()) {
            if (reparatur.getBestellung() != null && reparatur.getBestellung().getId() == null) {
                reparatur.setBestellung(null);
            }
            final boolean isErstellen = reparatur.getId() == null;
            if (isErstellen) {
                reparatur.setErstelltAm(LocalDateTime.now());
                if (StringUtils.isBlank(reparatur.getMitarbeiter())) {
                    reparatur.setMitarbeiter(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
                }
                reparatur.setMitarbeiter(StringUtils.abbreviate(reparatur.getMitarbeiter(), Reparatur.MAX_LENGTH_MITARBEITER));
                final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
                if (optional.isPresent()) {
                    reparatur.setFiliale(optional.get().getFiliale());
                }
            }
            final Reparatur saved = service.save(reparatur);
            bestellungErledigen(saved);
            if (isErstellen && reparatur.getKunde() != null && !reparatur.getKunde().isDsgvo()) {
                kundeService.saveDsgvo(reparatur.getKunde().getId());
                result.put(Message.INFO.getCode(), messageService.get("dsgvo.info"));
            } else {
                result.put(Message.SUCCESS.getCode(), messageService.get("reparatur.save.success", reparatur.getNummer()));
            }
            result.put("reparatur", saved);
            protokollService.write(saved.getId(), REPARATUR, saved.getNummer(), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    private void bestellungErledigen(final Reparatur saved) {
        if (saved.getBestellung() != null && saved.getBestellung().getId() != null) {
            final Optional<Bestellung> optional = bestellungService.getBestellung(saved.getBestellung().getId());
            if (optional.isPresent()) {
                bestellungService.bestellungErledigen(optional.get(), true);
            }
        }
    }

    @PutMapping("/erledigen")
    public Map<String, Object> reparaturErledigen(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        if (obj.getId() == null) {
            result.put(Message.ERROR.getCode(), messageService.get("reparatur.save.error"));
        } else {
            final Optional<Reparatur> optional = service.getReparatur(obj.getId());
            if (optional.isPresent()) {
                final Reparatur reparatur = optional.get();
                final boolean erledigt = !reparatur.isErledigt();
                service.reparaturErledigen(reparatur, erledigt);
                protokollService.write(reparatur.getId(), REPARATUR,
                        messageService.get("protokoll.erledigt", reparatur.getNummer(), BooleanUtils.toStringYesNo(erledigt)), GEAENDERT);
                result.put(Message.SUCCESS.getCode(), messageService.get("reparatur.save.success", reparatur.getNummer()));
            }
        }
        return result;
    }

    @PutMapping("/neu")
    public Map<String, Object> reparaturNeu(@RequestBody final IntegerBaseObject obj) {
        if (obj.getId() != null) {
            service.getReparatur(obj.getId()).ifPresent(reparatur -> {
                reparatur.setNeu(false);
                service.save(reparatur);
            });
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), true);
    }

    @PutMapping("/geraet-erhalten")
    public Map<String, Object> geraetErhalten(@RequestBody final IntegerBaseObject obj) {
        if (obj.getId() != null) {
            service.getReparatur(obj.getId()).ifPresent(reparatur -> {
                service.geraetErhalten(reparatur);
            });
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("reparatur.geraetErhalten.success"));
    }

    @DeleteMapping
    public Map<String, Object> deleteReparatur(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Reparatur> optional = service.getReparatur(id);
        service.deleteReparatur(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), REPARATUR, optional.get().getNummer(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("reparatur.delete.success"));
    }

    @GetMapping("/mitarbeiter")
    public List<DefaultKeyValue<Integer, String>> getMitarbeiter() {
        return einstellungenService.getMitarbeiter();
    }

    @GetMapping("/reparaturarten")
    public List<DefaultKeyValue<Integer, String>> getReparaturarten() {
        return service.getReparaturarten();
    }

    @GetMapping("/geraetepasswortarten")
    public List<DefaultKeyValue<Integer, String>> getGeraetepasswortarten() {
        return service.getGeraetepasswortarten();
    }

    @GetMapping("/pruefstatus")
    public List<DefaultKeyValue<Integer, String>> getPruefstatus() {
        return service.getPruefstatus();
    }

}
