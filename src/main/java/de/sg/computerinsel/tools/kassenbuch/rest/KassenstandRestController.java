package de.sg.computerinsel.tools.kassenbuch.rest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.kassenbuch.rest.model.Kassenstand;
import de.sg.computerinsel.tools.kassenbuch.service.KassenstandService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.ProtokollService;

/**
 * @author Sita Geßner
 */
@RestController
@RequestMapping("/kassenbuch/kassenstand")
public class KassenstandRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private KassenstandService service;

    @Autowired
    private ProtokollService protokollService;

    @GetMapping
    public List<Kassenstand> get() {
        protokollService.write("Kassenstand eingesehen");
        return einstellungenService.getKassenstand();
    }

    @PutMapping
    public Map<String, Object> save(@RequestBody final List<Kassenstand> kassenstaende) {
        einstellungenService.saveKassenstand(kassenstaende);
        service.ablegen(kassenstaende);
        protokollService.write("Kassenstand gespeichert");
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Der Kassenstand wurde erfolgreich gespeichert.");
    }

}
