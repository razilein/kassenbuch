package de.sg.computerinsel.tools.rechnung.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.RECHNUNG;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.service.ProtokollService;

@RestController
@RequestMapping("/rechnung")
public class RechnungRestController {

    @Autowired
    private InventarService inventarService;

    @Autowired
    private RechnungService service;

    @Autowired
    private ProtokollService protokollService;

    @PostMapping
    public Page<Rechnung> list(@RequestBody final SearchData data) {
        return service.listRechnungen(data.getData().getPagination(), data.getConditions());
    }

    @PostMapping("/produkt")
    public Page<Produkt> listProdukte(@RequestBody final SearchData data) {
        return inventarService.listProdukte(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/zahlarten")
    public List<DefaultKeyValue<Integer, String>> getZahlarten() {
        return service.getZahlarten();
    }

    @GetMapping("/kategorie")
    public List<DefaultKeyValue<Integer, String>> listKategorien() {
        return inventarService.listKategorien();
    }

    @GetMapping("/gruppe/{kategorieId}")
    public List<DefaultKeyValue<Integer, String>> listGruppen(@PathVariable final Integer kategorieId) {
        return inventarService.listGruppen(kategorieId);
    }

    @GetMapping("/{id}")
    public RechnungDTO get(@PathVariable final Integer id) {
        final RechnungDTO dto = service.getRechnung(id);
        if (dto.getRechnung().getId() != null) {
            protokollService.write(dto.getRechnung().getId(), RECHNUNG, String.valueOf(dto.getRechnung().getNummer()), ANGESEHEN);
        }
        return dto;
    }

    @PutMapping
    public Map<String, Object> save(@RequestBody final RechnungDTO dto) {
        final Rechnung rechnung = dto.getRechnung();
        final Map<String, Object> result = new HashMap<>(ValidationUtils.validate(rechnung));
        if (rechnung.getReparatur() != null && rechnung.getReparatur().getId() == null) {
            rechnung.setReparatur(null);
        }
        if (rechnung.getKunde() != null && rechnung.getKunde().getId() == null) {
            rechnung.setKunde(null);
        }

        for (final Rechnungsposten posten : dto.getPosten()) {
            result.putAll(ValidationUtils.validate(posten));
        }
        if (result.isEmpty()) {
            final Rechnung saved = service.saveRechnung(rechnung);
            for (final Rechnungsposten posten : dto.getPosten()) {
                posten.setRechnung(saved);
                service.savePosten(posten);
            }
            result.put(Message.SUCCESS.getCode(), "Die Rechnung " + saved.getNummer() + " erfolgreich gespeichert.");
            result.put("rechnung", saved);
            protokollService.write(saved.getId(), RECHNUNG, String.valueOf(rechnung.getNummer()),
                    rechnung.getId() == null ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final RechnungDTO dto = service.getRechnung(id);
        service.deleteRechnung(id);
        if (dto.getRechnung().getId() != null) {
            protokollService.write(dto.getRechnung().getId(), RECHNUNG, String.valueOf(dto.getRechnung().getNummer()), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Die Rechnung wurde erfolgreich gelöscht.");
    }

}