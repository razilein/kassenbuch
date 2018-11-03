package de.sg.computerinsel.tools.reparatur.rest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.rest.ValidationUtils;

@RestController
@RequestMapping("/reparatur")
public class ReparaturRestController {

    @Autowired
    private ReparaturService service;

    @PostMapping("/kunde")
    public Page<Kunde> getKunden(@RequestBody final SearchData data) {
        return service.listKunden(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/kunde/{id}")
    public Kunde getKunde(@PathVariable final Integer id) {
        return service.getKunde(id).orElse(new Kunde());
    }

    @PutMapping("/kunde")
    public Map<String, Object> saveKunde(@RequestBody final Kunde kunde) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(ValidationUtils.validate(kunde));

        if (result.isEmpty()) {
            if (kunde.getId() == null) {
                kunde.setErstelltAm(LocalDateTime.now());
            }
            service.save(kunde);
            result.put(Message.SUCCESS.getCode(), "Der Kunde '" + kunde.getNachname() + "' wurde erfolgreich gespeichert");
        }
        return result;
    }

    @DeleteMapping("/kunde")
    public Map<String, Object> deleteKunde(@RequestBody final Map<String, Object> data) {
        service.deleteKunde((int) data.get("id"));
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Der Kunde wurde erfolgreich gelöscht.");
    }

}
