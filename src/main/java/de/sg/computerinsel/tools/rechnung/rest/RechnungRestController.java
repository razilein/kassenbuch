package de.sg.computerinsel.tools.rechnung.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.RECHNUNG;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.REPARATUR;
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
import org.apache.commons.lang3.BooleanUtils;
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

import de.sg.computerinsel.tools.bestellung.service.BestellungService;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.service.ProtokollService;

@RestController
@RequestMapping("/rechnung")
public class RechnungRestController {

    @Autowired
    private BestellungService bestellungService;

    @Autowired
    private InventarService inventarService;

    @Autowired
    private RechnungService service;

    @Autowired
    private ReparaturService reparaturService;

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

        final boolean isErstellt = dto.getRechnung().getId() == null;
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
            final Zahlart zahlart = Zahlart.getByCode(rechnung.getArt());
            if (isErstellt && (zahlart == Zahlart.BAR || zahlart == Zahlart.EC)) {
                rechnung.setBezahlt(true);
            }
            final Rechnung saved = service.saveRechnung(rechnung);
            savePosten(dto.getPosten(), saved);
            reparaturErledigen(saved);
            if (isErstellt) {
                inventarAnpassen(dto);
                bestellungService.saveBestellung(dto.getPosten());
            }
            result.put(Message.SUCCESS.getCode(), "Die Rechnung " + saved.getNummer() + " erfolgreich gespeichert.");
            result.put("rechnung", saved);
            protokollService.write(saved.getId(), RECHNUNG, String.valueOf(rechnung.getNummer()), isErstellt ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @PutMapping("/bezahlt")
    public Map<String, Object> rechnungBezahlt(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        if (obj.getId() == null) {
            result.put(Message.ERROR.getCode(), "Ungültige Rechnung");
        } else {
            final Rechnung rechnung = service.getRechnung(obj.getId()).getRechnung();
            if (rechnung.getId() != null) {
                final boolean bezahlt = !rechnung.isBezahlt();
                service.rechnungBezahlt(rechnung, bezahlt);
                protokollService.write(rechnung.getId(), REPARATUR,
                        rechnung.getNummer() + " Erledigt: " + BooleanUtils.toStringYesNo(bezahlt), GEAENDERT);
            }
            result.put(Message.SUCCESS.getCode(), "Die Rechnung '" + rechnung.getNummer() + "' wurde erfolgreich gespeichert");
        }
        return result;
    }

    private void inventarAnpassen(final RechnungDTO dto) {
        inventarService.bestandReduzieren(dto.getPosten());
    }

    private void savePosten(final List<Rechnungsposten> list, final Rechnung rechnung) {
        for (final Rechnungsposten posten : list) {
            posten.setRechnung(rechnung);
            service.savePosten(posten);
        }
    }

    private void reparaturErledigen(final Rechnung saved) {
        if (saved.getReparatur() != null && saved.getReparatur().getId() != null) {
            final Optional<Reparatur> optional = reparaturService.getReparatur(saved.getReparatur().getId());
            if (optional.isPresent()) {
                reparaturService.reparaturErledigen(optional.get(), true);
            }
        }
    }

    @DeleteMapping
    public Map<String, Object> delete(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final RechnungDTO dto = service.getRechnung(id);
        service.deleteRechnung(id);
        if (dto.getRechnung().getId() != null) {
            protokollService.write(dto.getRechnung().getId(), RECHNUNG, String.valueOf(dto.getRechnung().getNummer()), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(),
                "Die Rechnung " + dto.getRechnung().getNummer() + " wurde erfolgreich gelöscht.");
    }

}
