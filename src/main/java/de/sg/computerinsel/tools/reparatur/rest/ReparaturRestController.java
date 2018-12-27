package de.sg.computerinsel.tools.reparatur.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.KUNDE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.REPARATUR;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.ReparaturArt;
import de.sg.computerinsel.tools.reparatur.service.FeiertagUtils;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.ProtokollService;

@RestController
@RequestMapping("/reparatur")
public class ReparaturRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private ReparaturService service;

    @PostMapping
    public Page<Reparatur> getReparaturen(@RequestBody final SearchData data) {
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
        return optional.orElseGet(this::createReparatur);
    }

    private Reparatur createReparatur() {
        final Reparatur reparatur = new Reparatur();
        reparatur.setMitarbeiter(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
        reparatur.setKunde(new Kunde());
        reparatur.setAbholdatum(berechneAbholdatum(false));
        reparatur.setAbholzeit(berechneAbholzeit(false));
        reparatur.setArt(ReparaturArt.REPARATUR.getCode());
        return reparatur;
    }

    @GetMapping("/abholdatum/{express}")
    public Map<String, Object> getAbholdatumUndZeit(@PathVariable final boolean express) {
        final Map<String, Object> result = new HashMap<>();
        result.put("abholdatum", berechneAbholdatum(express));
        result.put("abholzeit", berechneAbholzeit(express).format(DateTimeFormatter.ofPattern("HH:mm")));
        return result;
    }

    private LocalDate berechneAbholdatum(final boolean express) {
        LocalDate date = LocalDate.now();
        if (express && LocalTime.now().isAfter(LocalTime.of(13, 30))) {
            date = date.plusDays(1);
        } else if (!express) {
            date = date.plusDays(3);
        }
        while (date.getDayOfWeek() == DayOfWeek.SUNDAY || FeiertagUtils.isFeiertag(date)) {
            date = date.plusDays(1);
        }
        return date;
    }

    private LocalTime berechneAbholzeit(final boolean express) {
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

    @PutMapping
    public Map<String, Object> saveReparatur(@RequestBody final Reparatur reparatur) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(ValidationUtils.validate(reparatur));
        if (result.isEmpty()) {
            final boolean isErstellen = reparatur.getId() == null;
            if (isErstellen) {
                reparatur.setErstelltAm(LocalDateTime.now());
                reparatur.setMitarbeiter(StringUtils.abbreviate(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname(),
                        Reparatur.MAX_LENGTH_MITARBEITER));
            }
            final Reparatur saved = service.save(reparatur);
            result.put(Message.SUCCESS.getCode(), "Der Reparaturauftrag '" + reparatur.getNummer() + "' wurde erfolgreich gespeichert");
            result.put("reparatur", saved);
            protokollService.write(saved.getId(), REPARATUR, saved.getNummer(), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @PutMapping("/erledigen")
    public Map<String, Object> reparaturErledigen(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        if (obj.getId() == null) {
            result.put(Message.ERROR.getCode(), "Ungültiger Reparaturauftrag");
        } else {
            final Optional<Reparatur> optional = service.getReparatur(obj.getId());
            if (optional.isPresent()) {
                final Reparatur reparatur = optional.get();
                final boolean erledigt = !reparatur.isErledigt();
                reparatur.setGeraetepasswort(null);
                reparatur.setErledigt(erledigt);
                reparatur.setErledigungsdatum(erledigt ? LocalDateTime.now() : null);
                service.save(reparatur);
                protokollService.write(reparatur.getId(), REPARATUR,
                        reparatur.getNummer() + " Erledigt: " + BooleanUtils.toStringYesNo(erledigt), GEAENDERT);
                result.put(Message.SUCCESS.getCode(), "Der Reparaturauftrag '" + reparatur.getNummer() + "' wurde erfolgreich gespeichert");
            }
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> deleteReparatur(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Reparatur> optional = service.getReparatur(id);
        service.deleteReparatur(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), REPARATUR, optional.get().getNummer(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Der Reparaturauftrag wurde erfolgreich gelöscht.");
    }

    @GetMapping("/mitarbeiter")
    public List<DefaultKeyValue<Integer, String>> getMitarbeiter() {
        return einstellungenService.getMitarbeiter();
    }

    @GetMapping("/reparaturarten")
    public List<DefaultKeyValue<Integer, String>> getReparaturarten() {
        return service.getReparaturarten();
    }

    @PostMapping("/kunde")
    public Page<Kunde> getKunden(@RequestBody final SearchData data) {
        return service.listKunden(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/kunde/download-dsgvo/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable final Integer id) throws IOException {
        service.saveDsgvo(id);
        final File file = new File(einstellungenService.getDsgvoFilepath());
        final Path path = Paths.get(einstellungenService.getDsgvoFilepath()).toAbsolutePath();
        final ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        final HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Einwilligung_DSGVO.pdf");
        return ResponseEntity.ok().headers(header).contentLength(file.length()).contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }

    @GetMapping("/kunde/{id}")
    public Kunde getKunde(@PathVariable final Integer id) {
        final Optional<Kunde> optional = service.getKunde(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KUNDE, optional.get().getNummer().toString(), ANGESEHEN);
        }
        return optional.orElse(new Kunde());
    }

    @PutMapping("/kunde")
    public Map<String, Object> saveKunde(@RequestBody final Kunde kunde) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(ValidationUtils.validate(kunde));

        if (result.isEmpty()) {
            final boolean isErstellen = kunde.getId() == null;
            if (isErstellen) {
                kunde.setErstelltAm(LocalDateTime.now());
            }
            final Kunde saved = service.save(kunde);
            result.put(Message.SUCCESS.getCode(), "Der Kunde '" + saved.getNummer() + "' wurde erfolgreich gespeichert");
            result.put("kunde", saved);
            protokollService.write(saved.getId(), KUNDE, saved.getNummer().toString(), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping("/kunde")
    public Map<String, Object> deleteKunde(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");

        final Optional<Kunde> optional = service.getKunde(id);
        service.deleteReparatur(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KUNDE, optional.get().getNummer().toString(), GELOESCHT);
        }
        service.deleteKunde(id);
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Der Kunde wurde erfolgreich gelöscht.");
    }

}
