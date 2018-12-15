package de.sg.computerinsel.tools.inventar.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.PRODUKT;
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
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.service.ProtokollService;

@RestController
@RequestMapping("/inventar/produkt")
public class ProduktRestController {

    @Autowired
    private InventarService service;

    @Autowired
    private ProtokollService protokollService;

    @GetMapping("/kategorie")
    public List<DefaultKeyValue<Integer, String>> listKategorien() {
        return service.listKategorien();
    }

    @GetMapping("/gruppe/{kategorieId}")
    public List<DefaultKeyValue<Integer, String>> listGruppen(@PathVariable final Integer kategorieId) {
        return service.listGruppen(kategorieId);
    }

    @PostMapping
    public Page<Produkt> list(@RequestBody final SearchData data) {
        return service.listProdukte(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/{id}")
    public Produkt get(@PathVariable final Integer id) {
        final Optional<Produkt> optional = service.getProdukt(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), PRODUKT, optional.get().getBezeichnung(), ANGESEHEN);
        }
        return optional.orElseGet(ProduktRestController::createProdukt);
    }

    private static Produkt createProdukt() {
        final Produkt produkt = new Produkt();
        final Gruppe gruppe = new Gruppe();
        gruppe.setKategorie(new Kategorie());
        produkt.setGruppe(gruppe);
        return produkt;
    }

    @PutMapping
    public Map<String, Object> save(@RequestBody final Produkt produkt) {
        final Map<String, Object> result = new HashMap<>(ValidationUtils.validate(produkt));
        if (result.isEmpty()) {
            final Produkt saved = service.saveProdukt(produkt);
            result.put(Message.SUCCESS.getCode(), "Das Produkt wurde erfolgreich gespeichert.");
            protokollService.write(saved.getId(), PRODUKT, produkt.getBezeichnung(), produkt.getId() == null ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Produkt> optional = service.getProdukt(id);
        service.deleteProdukt(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), PRODUKT, optional.get().getBezeichnung(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Das Produkt wurde erfolgreich gelöscht.");
    }

}