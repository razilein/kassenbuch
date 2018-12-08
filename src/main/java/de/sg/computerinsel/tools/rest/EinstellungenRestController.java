package de.sg.computerinsel.tools.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/einstellungen")
@Slf4j
public class EinstellungenRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

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
        return einstellungenService.getFiliale(id).orElse(new Filiale());
    }

    @PutMapping("/filiale")
    public Map<String, Object> saveFiliale(@RequestBody final Filiale filiale) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(ValidationUtils.validate(filiale));

        if (result.isEmpty()) {
            einstellungenService.save(filiale);
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
        return einstellungenService.getMitarbeiter(id).map(MitarbeiterDTO::new).orElseGet(MitarbeiterDTO::new);
    }

    @PutMapping("/mitarbeiter")
    public Map<String, Object> saveMitarbeiter(@RequestBody final MitarbeiterDTO dto) {
        final Optional<Mitarbeiter> optional = dto.getId() == null ? Optional.of(new Mitarbeiter(dto))
                : einstellungenService.getMitarbeiter(dto.getId());
        return mitarbeiterService.saveMitarbeiterProfil(dto, optional);
    }

    @PutMapping("/mitarbeiter/reset")
    public Map<String, Object> resetMitarbeiter(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        final Optional<Mitarbeiter> optional = einstellungenService.getMitarbeiter(obj.getId());
        if (optional.isPresent()) {
            final Mitarbeiter mitarbeiter = optional.get();
            mitarbeiter.setPasswort(null);
            einstellungenService.save(mitarbeiter);
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
        return mitarbeiterService.listRollen(id);
    }

    @PutMapping("/mitarbeiter/rechte")
    public Map<String, Object> saveMitarbeiterRollen(@RequestBody final MitarbeiterRollenDTO dto) {
        final Map<String, Object> result = new HashMap<>();
        mitarbeiterService.saveRollen(dto);
        result.put(Message.SUCCESS.getCode(), "Die Berechtigungen wurden erfolgreich gespeichert.");
        return result;
    }

}
