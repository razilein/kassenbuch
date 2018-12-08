package de.sg.computerinsel.tools.reparatur.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.KUNDE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.REPARATUR;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.io.IOUtils;
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

import de.sg.computerinsel.tools.reparatur.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
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
        return service.listReparaturen(data.getData().getPagination(), data.getConditions());
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
        reparatur.setMitarbeiter(new Mitarbeiter());
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
                reparatur.setMitarbeiter(mitarbeiterService.getAngemeldeterMitarbeiter().orElse(null));
            }
            final Reparatur saved = service.save(reparatur);
            result.put(Message.SUCCESS.getCode(), "Der Reparaturauftrag '" + reparatur.getNummer() + "' wurde erfolgreich gespeichert");
            result.put("reparatur", saved);
            protokollService.write(saved.getId(), REPARATUR, saved.getNummer(), isErstellen ? ERSTELLT : GEAENDERT);
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
    public void getFile(final HttpServletResponse response, @PathVariable final Integer id) throws IOException {
        try (final InputStream stream = new FileInputStream(einstellungenService.getDsgvoFilepath())) {
            IOUtils.copy(stream, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
        }
        service.saveDsgvo(id);
    }

    @GetMapping("/kunde/{id}")
    public Kunde getKunde(@PathVariable final Integer id) {
        final Optional<Kunde> optional = service.getKunde(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KUNDE, optional.get().getCompleteWithAdressAndPhone(), ANGESEHEN);
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
            result.put(Message.SUCCESS.getCode(), "Der Kunde '" + kunde.getNachname() + "' wurde erfolgreich gespeichert");
            result.put("kunde", saved);
            protokollService.write(saved.getId(), KUNDE, saved.getCompleteWithAdressAndPhone(), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping("/kunde")
    public Map<String, Object> deleteKunde(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");

        final Optional<Kunde> optional = service.getKunde(id);
        service.deleteReparatur(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KUNDE, optional.get().getCompleteWithAdressAndPhone(), GELOESCHT);
        }
        service.deleteKunde(id);
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Der Kunde wurde erfolgreich gelöscht.");
    }

}
