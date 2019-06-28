package de.sg.computerinsel.tools.inventar.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.GRUPPE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
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

import de.sg.computerinsel.tools.inventar.model.Gruppe;
import de.sg.computerinsel.tools.inventar.model.Kategorie;
import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;

@RestController
@RequestMapping("/inventar/gruppe")
public class GruppeRestController {

    @Autowired
    private InventarService service;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private ValidationService validationService;

    @GetMapping("/kategorie")
    public List<DefaultKeyValue<Integer, String>> listKategorien() {
        return service.listKategorien();
    }

    @PostMapping
    public Page<Gruppe> list(@RequestBody final SearchData data) {
        return service.listGruppen(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/{id}")
    public Gruppe get(@PathVariable final Integer id) {
        final Optional<Gruppe> optional = service.getGruppe(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), GRUPPE, optional.get().getBezeichnung(), ANGESEHEN);
        }
        return optional.orElseGet(GruppeRestController::createGruppe);
    }

    private static Gruppe createGruppe() {
        final Gruppe gruppe = new Gruppe();
        gruppe.setKategorie(new Kategorie());
        return gruppe;
    }

    @PutMapping
    public Map<String, Object> save(@RequestBody final Gruppe gruppe) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(gruppe));
        if (result.isEmpty()) {
            final Gruppe saved = service.saveGruppe(gruppe);
            result.put(Message.SUCCESS.getCode(), messageService.get("inventar.gruppe.save.success"));
            protokollService.write(saved.getId(), GRUPPE, gruppe.getBezeichnung(), gruppe.getId() == null ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Gruppe> optional = service.getGruppe(id);
        service.deleteGruppe(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), GRUPPE, optional.get().getBezeichnung(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("inventar.gruppe.delete.success"));
    }

}
