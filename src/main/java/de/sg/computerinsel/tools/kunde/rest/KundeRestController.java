package de.sg.computerinsel.tools.kunde.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.KUNDE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;

@RestController
@RequestMapping("/kunde")
public class KundeRestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private KundeService service;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<Kunde> getKunden(@RequestBody final SearchData data) {
        return service.listKunden(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/{id}")
    public Kunde getKunde(@PathVariable final Integer id) {
        final Optional<Kunde> optional = service.getKunde(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KUNDE, optional.get().getNummer().toString(), ANGESEHEN);
        }
        return optional.orElse(new Kunde());
    }

    @PutMapping
    public Map<String, Object> saveKunde(@RequestBody final Kunde kunde) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(kunde));
        if (result.isEmpty()) {
            final boolean isErstellen = kunde.getId() == null;
            if (isErstellen) {
                kunde.setErstelltAm(LocalDateTime.now());
            }
            final Kunde saved = service.save(kunde);
            result.put(Message.SUCCESS.getCode(), messageService.get("kunde.save.success", saved.getNummer()));
            result.put("kunde", saved);
            protokollService.write(saved.getId(), KUNDE, saved.getNummer().toString(), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> deleteKunde(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");

        final Optional<Kunde> optional = service.getKunde(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KUNDE, optional.get().getNummer().toString(), GELOESCHT);
        }
        service.deleteKunde(id);
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("kunde.delete.success"));
    }

}
