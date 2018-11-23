package de.sg.computerinsel.tools.kassenbuch.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchEintragungCsvDatei;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchEintragungManuell;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchErstellenData;
import de.sg.computerinsel.tools.kassenbuch.service.KassenbuchErstellenService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.service.EinstellungenService;

/**
 * @author Sita Geßner
 */
@RestController
@RequestMapping("/kassenbuch")
public class KassenbuchRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private KassenbuchErstellenService service;

    @GetMapping("/ausgangsbetrag")
    public String getAusgangsbetrag() {
        return einstellungenService.getAusgangsbetrag().getWert();
    }

    @GetMapping("/csv")
    public String getLetzteCsvDateiPfad() {
        return einstellungenService.getLetzteCsvDateiPfad().getWert();
    }

    @PostMapping("/erstellen")
    public Map<String, Object> erstelleKassenbuch(@RequestBody final KassenbuchErstellenData data) {
        final Map<String, Object> result = new HashMap<>(ValidationUtils.validate(data));

        final String rechnungsverzeichnis = einstellungenService.getRechnungsverzeichnis().getWert();
        final String ablageverzeichnis = einstellungenService.getAblageverzeichnis().getWert();

        result.putAll(ValidationUtils.validateVerzeichnisse(rechnungsverzeichnis, ablageverzeichnis));

        if (!result.containsKey(Message.ERROR.getCode())) {
            result.putAll(service.startRechnungsablage(rechnungsverzeichnis, ablageverzeichnis, data));
        }

        return result;
    }

    @GetMapping("/download/")
    public void getFile(final HttpServletResponse response) throws IOException {
        try (final InputStream stream = new FileInputStream(einstellungenService.getLetztePdfDateiPfad().getWert())) {
            IOUtils.copy(stream, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
        }
    }

    @PostMapping("/eintragungen/erstellen")
    public Map<String, Object> erstelleEintragungen(@RequestBody final KassenbuchEintragungCsvDatei eintragungCsvDatei) {
        final Map<String, Object> result = new HashMap<>();

        final List<KassenbuchEintragungManuell> eintragungen = eintragungCsvDatei.getEintragungen();
        for (final KassenbuchEintragungManuell eintragung : eintragungen) {
            result.putAll(ValidationUtils.validate(eintragung));
        }

        final String rechnungsverzeichnis = einstellungenService.getRechnungsverzeichnis().getWert();
        final String ablageverzeichnis = einstellungenService.getAblageverzeichnis().getWert();

        result.putAll(ValidationUtils.validateVerzeichnisse(rechnungsverzeichnis, ablageverzeichnis));
        if (!new File(eintragungCsvDatei.getCsvDatei()).exists()) {
            result.put(Message.ERROR.getCode(), "Der CSV-Dateipfad '" + eintragungCsvDatei.getCsvDatei() + "' existiert nicht.");
        }

        final List<KassenbuchEintragungManuell> nochNichtGespeicherteEintragungen = eintragungen.stream().filter(e -> !e.isGespeichert())
                .collect(Collectors.toList());
        if (nochNichtGespeicherteEintragungen.isEmpty()) {
            result.put(Message.INFO.getCode(), "Es wurden keine Eintragungen erstellt oder alle Eintragungen wurden bereits gespeichert.");
        } else if (!result.containsKey(Message.ERROR.getCode())) {
            service.manuelleEintragung(nochNichtGespeicherteEintragungen, eintragungCsvDatei.getCsvDatei());
            result.put(Message.SUCCESS.getCode(), "Die Eintragungen wurden erfolgreich hinzugefügt.");
        }

        return result;
    }

}
