package de.sg.computerinsel.tools.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.rest.model.EinstellungenData;
import de.sg.computerinsel.tools.rest.model.TableData;
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
        return data;
    }

    @PutMapping
    public Map<String, Object> saveEinstellungen(@RequestBody final EinstellungenData data) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(
                ValidationUtils.validateVerzeichnisse(data.getRechnungsverzeichnis().getWert(), data.getAblageverzeichnis().getWert()));

        if (result.isEmpty()) {
            einstellungenService.save(data.getAblageverzeichnis());
            einstellungenService.save(data.getRechnungsverzeichnis());
            result.put(Message.SUCCESS.getCode(), "Die Einstellungen wurden erfolgreich gespeichert.");
        }

        return result;
    }

    @PostMapping("/filiale")
    public Page<Filiale> getFilialen(@RequestBody final TableData data) {
        return einstellungenService.listFiliale(data.getPagination());
    }

}
