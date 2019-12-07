package de.sg.computerinsel.tools.einkauf.rest;

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

import de.sg.computerinsel.tools.einkauf.service.EinkaufService;
import de.sg.computerinsel.tools.einkauf.service.FtpService;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/einkauf")
@Slf4j
public class EinkaufRestController {

    @Autowired
    private EinkaufService einkaufService;

    @Autowired
    private FtpService ftpService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @GetMapping
    public String getBestellliste() {
        return einkaufService.getEinkaufslisteAsText();
    }

    @GetMapping("/download")
    public String downloadEinkaufsliste() {
        final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
        if (optional.isPresent()) {
            final String dateiname = getDateinameByFiliale(optional.get());
            return ftpService.downloadEinkaufsliste(dateiname);
        }
        return StringUtils.EMPTY;
    }

    private String getDateinameByFiliale(final Mitarbeiter mitarbeiter) {
        return mitarbeiter.getFiliale().getOrt().toLowerCase() + ".txt";
    }

    @PutMapping
    public Map<String, Object> uploadEinkaufsliste(@RequestBody final String text) {
        final Map<String, Object> result = new HashMap<>();

        final Optional<Mitarbeiter> optional = mitarbeiterService.getAngemeldeterMitarbeiter();
        if (optional.isPresent()) {
            final String dateiname = getDateinameByFiliale(optional.get());
            try {
                ftpService.uploadEinkaufsliste(dateiname, text);
                result.put(Message.SUCCESS.getCode(), messageService.get("einkaufsliste.put.success"));
            } catch (final IllegalStateException e) {
                log.debug(e.getMessage(), e);
                result.put(Message.ERROR.getCode(), e.getMessage());
            }
        } else {
            result.put(Message.ERROR.getCode(), messageService.get("einkaufsliste.put.error.filiale"));
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> clearEinkaeufe() {
        einkaufService.deleteAllEinkaeufe();
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("einkaufsliste.clear.success"));
    }
}
