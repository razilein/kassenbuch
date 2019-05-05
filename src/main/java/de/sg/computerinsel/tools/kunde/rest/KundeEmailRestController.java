package de.sg.computerinsel.tools.kunde.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.sg.computerinsel.tools.kunde.service.EmailService;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.rest.Message;

@RestController
@RequestMapping("/email")
public class KundeEmailRestController {

    @Autowired
    private EmailService service;

    @Autowired
    private RechnungService rechnungService;

    @Autowired
    private ReparaturService reparaturService;

    @PostMapping("/rechnung")
    public Map<String, Object> sendeMailRechnung(@RequestParam("file") final MultipartFile file, @RequestParam("id") final Integer id) {
        final Map<String, Object> result = new HashMap<>();

        final Rechnung rechnung = rechnungService.getRechnung(id).getRechnung();
        service.sendeEmail(file, rechnung);
        result.put(Message.SUCCESS.getCode(), "Die E-Mail wurde erfolgreich versendet.");
        return result;
    }

    @PostMapping("/reparatur")
    public Map<String, Object> sendeMailReparaturauftrag(@RequestBody final Map<String, Integer> params) {
        final Map<String, Object> result = new HashMap<>();

        final Integer id = params.get("id");
        final Optional<Reparatur> optional = reparaturService.getReparatur(id);
        if (optional.isPresent()) {
            service.sendeEmail(optional.get());
            result.put(Message.SUCCESS.getCode(), "Die E-Mail wurde erfolgreich versendet.");
        } else {
            result.put(Message.ERROR.getCode(), "Der Auftrag mit ID " + id
                    + " konnte nicht mehr gefunden werden, möglicherweise wurde der Auftrag zwischenzeitlich gelöscht.");
        }
        return result;
    }

}
