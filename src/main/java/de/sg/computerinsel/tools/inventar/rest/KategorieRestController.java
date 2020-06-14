package de.sg.computerinsel.tools.inventar.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.KATEGORIE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

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

import de.sg.computerinsel.tools.inventar.model.Kategorie;
import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.model.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;

@RestController
@RequestMapping("/inventar/kategorie")
public class KategorieRestController {

    @Autowired
    private InventarService service;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<Kategorie> list(@RequestBody final SearchData data) {
        return service.listKategorien(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/{id}")
    public Kategorie get(@PathVariable final Integer id) {
        final Optional<Kategorie> optional = service.getKategorie(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KATEGORIE, optional.get().getBezeichnung(), ANGESEHEN);
        }
        return optional.orElseGet(Kategorie::new);
    }

    @PutMapping
    public Map<String, Object> save(@RequestBody final Kategorie kategorie) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(kategorie));
        if (result.isEmpty()) {
            final Kategorie saved = service.saveKategorie(kategorie);
            result.put(Message.SUCCESS.getCode(), messageService.get("inventar.kategorie.save.success"));
            protokollService.write(saved.getId(), KATEGORIE, kategorie.getBezeichnung(), kategorie.getId() == null ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Kategorie> optional = service.getKategorie(id);
        service.deleteKategorie(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), KATEGORIE, optional.get().getBezeichnung(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("inventar.kategorie.delete.success"));
    }

}
