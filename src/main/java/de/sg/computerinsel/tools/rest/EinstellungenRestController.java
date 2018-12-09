package de.sg.computerinsel.tools.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.FILIALE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.MITARBEITER;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.RECHTE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
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

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.model.EinstellungenData;
import de.sg.computerinsel.tools.rest.model.MitarbeiterDTO;
import de.sg.computerinsel.tools.rest.model.MitarbeiterRollenDTO;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.ProtokollService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/einstellungen")
@Slf4j
public class EinstellungenRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @Autowired
    private ProtokollService protokollService;

    @GetMapping
    public EinstellungenData getEinstellungen() {
        final EinstellungenData data = new EinstellungenData();
        data.setAblageverzeichnis(einstellungenService.getAblageverzeichnis());
        data.setRechnungsverzeichnis(einstellungenService.getRechnungsverzeichnis());
        data.setFiliale(einstellungenService.getFiliale());
        return data;
    }

    @GetMapping("/standardfiliale")
    public Filiale getStandardFiliale() {
        final String id = einstellungenService.getFiliale().getWert();
        return getFiliale(id == null ? null : Ints.tryParse(id));
    }

    @PutMapping
    public Map<String, Object> saveEinstellungen(@RequestBody final EinstellungenData data) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(
                ValidationUtils.validateVerzeichnisse(data.getRechnungsverzeichnis().getWert(), data.getAblageverzeichnis().getWert()));

        if (StringUtils.isBlank(data.getFiliale().getWert())) {
            result.put(Message.ERROR.getCode(), "Bitte wählen Sie eine Filiale aus."
                    + " Sollte keine Filiale zur Auswahl stehen müssen Sie diese zuerst in den Einstellungen unter Filiale eine Filiale anlegen");
        }

        if (result.isEmpty()) {
            einstellungenService.save(data.getAblageverzeichnis());
            einstellungenService.save(data.getRechnungsverzeichnis());
            einstellungenService.save(data.getFiliale());
            result.put(Message.SUCCESS.getCode(), "Die Einstellungen wurden erfolgreich gespeichert.");
            protokollService.write("Einstellungen gespeichert");
        }

        return result;
    }

    @GetMapping("/filiale")
    public List<DefaultKeyValue<Integer, String>> getFilialen() {
        return einstellungenService.listFiliale();
    }

    @PostMapping("/filiale")
    public Page<Filiale> getFilialen(@RequestBody final SearchData data) {
        return einstellungenService.listFiliale(data.getData().getPagination());
    }

    @GetMapping("/filiale/{id}")
    public Filiale getFiliale(@PathVariable final Integer id) {
        final Optional<Filiale> optional = einstellungenService.getFiliale(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), FILIALE, optional.get().getName(), ANGESEHEN);
        }
        return optional.orElse(new Filiale());
    }

    @PutMapping("/filiale")
    public Map<String, Object> saveFiliale(@RequestBody final Filiale filiale) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(ValidationUtils.validate(filiale));

        if (result.isEmpty()) {
            final Filiale saved = einstellungenService.save(filiale);
            protokollService.write(saved.getId(), FILIALE, saved.getName(), filiale.getId() == null ? ERSTELLT : GEAENDERT);
            result.put(Message.SUCCESS.getCode(), "Die Filiale " + filiale.getName() + " wurde erfolgreich gespeichert");
        }
        return result;
    }

    @PostMapping("/mitarbeiter")
    public Page<MitarbeiterDTO> getMitarbeiter(@RequestBody final SearchData data) {
        return einstellungenService.listMitarbeiter(data.getData().getPagination());
    }

    @GetMapping("/mitarbeiter/{id}")
    public MitarbeiterDTO getMitarbeiter(@PathVariable final Integer id) {
        final Optional<Mitarbeiter> optional = einstellungenService.getMitarbeiter(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), MITARBEITER, optional.get().getCompleteName(), ANGESEHEN);
        }
        return optional.map(MitarbeiterDTO::new).orElseGet(MitarbeiterDTO::new);
    }

    @PutMapping("/mitarbeiter")
    public Map<String, Object> saveMitarbeiter(@RequestBody final MitarbeiterDTO dto) {
        final Optional<Mitarbeiter> optional = dto.getId() == null ? Optional.of(new Mitarbeiter(dto))
                : einstellungenService.getMitarbeiter(dto.getId());
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), MITARBEITER, "Profildaten geändert für: " + optional.get().getCompleteName(),
                    dto.getId() == null ? ERSTELLT : GEAENDERT);
        }
        return mitarbeiterService.saveMitarbeiterProfil(dto, optional);
    }

    @DeleteMapping("/mitarbeiter")
    public Map<String, Object> deleteMitarbeiter(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Mitarbeiter> optional = einstellungenService.getMitarbeiter(id);
        einstellungenService.deleteMitarbeiter(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), MITARBEITER, optional.get().getCompleteName(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Der Mitarbeiter wurde erfolgreich gelöscht.");
    }

    @PutMapping("/mitarbeiter/reset")
    public Map<String, Object> resetMitarbeiter(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        final Optional<Mitarbeiter> optional = einstellungenService.getMitarbeiter(obj.getId());
        if (optional.isPresent()) {
            final Mitarbeiter mitarbeiter = optional.get();
            mitarbeiter.setPasswort(null);
            einstellungenService.save(mitarbeiter);
            protokollService.write(mitarbeiter.getId(), MITARBEITER, "Passwort zurückgesetzt für: " + optional.get().getCompleteName(),
                    GEAENDERT);
            result.put(Message.SUCCESS.getCode(), "Das Passwort wurde zurückgesetzt.");
            log.debug("Passwort für Benutzer '{}' zurückgesetzt.", mitarbeiter.getBenutzername());
        } else {
            result.put(Message.ERROR.getCode(), "Der Benutzer konnte nicht gefunden werden.");
            log.debug("Benutzer mit ID '{}' nicht gefunden.", obj.getId());
        }
        return result;
    }

    @GetMapping("/mitarbeiter/rechte/{id}")
    public MitarbeiterRollenDTO getMitarbeiterRollen(@PathVariable final Integer id) {
        protokollService.write(id, RECHTE, ANGESEHEN);
        return mitarbeiterService.listRollen(id);
    }

    @PutMapping("/mitarbeiter/rechte")
    public Map<String, Object> saveMitarbeiterRollen(@RequestBody final MitarbeiterRollenDTO dto) {
        final Map<String, Object> result = new HashMap<>();
        mitarbeiterService.saveRollen(dto);
        protokollService.write(dto.getMitarbeiterId(), RECHTE, GEAENDERT);
        result.put(Message.SUCCESS.getCode(), "Die Berechtigungen wurden erfolgreich gespeichert.");
        return result;
    }

}
