package de.sg.computerinsel.tools.bestellung.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.bestellung.service.BestellungService;
import de.sg.computerinsel.tools.bestellung.service.FtpService;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bestellung")
@Slf4j
public class BestellungRestController {

    @Autowired
    private BestellungService bestellungService;

    @Autowired
    private FtpService ftpService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @GetMapping
    public String getBestellliste() {
        return bestellungService.getBestellungenAsText();
    }

    @GetMapping("/download")
    public String downloadBestellliste() {
        final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
        if (optional.isPresent()) {
            final String dateiname = getDateinameByFiliale(optional.get());
            return ftpService.downloadBestellliste(dateiname);
        }
        return StringUtils.EMPTY;
    }

    private String getDateinameByFiliale(final Mitarbeiter mitarbeiter) {
        return mitarbeiter.getFiliale().getOrt().toLowerCase() + ".txt";
    }

    @PutMapping
    public Map<String, Object> uploadBestellliste(@RequestBody final String text) {
        final Map<String, Object> result = new HashMap<>();

        final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
        if (optional.isPresent()) {
            final String dateiname = getDateinameByFiliale(optional.get());
            try {
                ftpService.uploadBestellliste(dateiname, text);
                result.put(Message.SUCCESS.getCode(), "Die Bestelliste wurde hochgeladen auf den FTP-Server.");
            } catch (final IllegalStateException e) {
                log.debug(e.getMessage(), e);
                result.put(Message.ERROR.getCode(), e.getMessage());
            }
        } else {
            result.put(Message.ERROR.getCode(),
                    "Die Bestellliste konnte nicht hochgeladen werden, da die Filiale des angemeldeten Benutzers nicht ermittelt werden konnte.");
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> clearBestellungen() {
        bestellungService.deleteAllBestellungen();
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Die Bestellliste wurde geleert.");
    }
}
