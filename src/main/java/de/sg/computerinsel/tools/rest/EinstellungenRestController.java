package de.sg.computerinsel.tools.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
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

import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.model.EinstellungenData;
import de.sg.computerinsel.tools.service.EinstellungenService;

@RestController
@RequestMapping("/einstellungen")
public class EinstellungenRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @GetMapping
    public EinstellungenData getEinstellungen() {
        final EinstellungenData data = new EinstellungenData();
        data.setAblageverzeichnis(einstellungenService.getAblageverzeichnis());
        data.setRechnungsverzeichnis(einstellungenService.getRechnungsverzeichnis());
        data.setFiliale(einstellungenService.getFiliale());
        return data;
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
    public List<DefaultKeyValue> getFilialen() {
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
    public Page<Mitarbeiter> getMitarbeiter(@RequestBody final SearchData data) {
        return einstellungenService.listMitarbeiter(data.getData().getPagination());
    }

    @GetMapping("/mitarbeiter/{id}")
    public Mitarbeiter getMitarbeiter(@PathVariable final Integer id) {
        return einstellungenService.getMitarbeiter(id).orElse(new Mitarbeiter());
    }

    @PutMapping("/mitarbeiter")
    public Map<String, Object> saveMitarbeiter(@RequestBody final Mitarbeiter mitarbeiter) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(ValidationUtils.validate(mitarbeiter));

        if (result.isEmpty()) {
            einstellungenService.save(mitarbeiter);
            result.put(Message.SUCCESS.getCode(), "Der Mitarbeiter '" + mitarbeiter.getCompleteName() + "' wurde erfolgreich gespeichert");
        }
        return result;
    }

}
