package de.sg.computerinsel.tools.rechnung.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.RECHNUNG;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.REPARATUR;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.STORNO;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
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

import de.sg.computerinsel.tools.angebot.dto.AngebotDto;
import de.sg.computerinsel.tools.angebot.service.AngebotService;
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.bestellung.service.BestellungService;
import de.sg.computerinsel.tools.einkauf.service.EinkaufService;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.RechnungView;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungExportDto;
import de.sg.computerinsel.tools.rechnung.rest.model.StornoDto;
import de.sg.computerinsel.tools.rechnung.service.RechnungExportService;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;
import de.sg.computerinsel.tools.stornierung.model.Stornierung;
import de.sg.computerinsel.tools.stornierung.service.StornierungService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rechnung")
@Slf4j
public class RechnungRestController {

    @Autowired
    private AngebotService angebotService;

    @Autowired
    private BestellungService bestellungService;

    @Autowired
    private EinkaufService einkaufService;

    @Autowired
    private InventarService inventarService;

    @Autowired
    private KundeService kundeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RechnungService service;

    @Autowired
    private ReparaturService reparaturService;

    @Autowired
    private RechnungExportService exportService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private StornierungService stornierungService;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<RechnungView> list(@RequestBody final SearchData data) {
        return service.listRechnungen(data.getData().getPagination(), data.getConditions());
    }

    @PostMapping("/produkt")
    public Page<Produkt> listProdukte(@RequestBody final SearchData data) {
        inventarService.checkAndSetSortierungAnzahlVerkaeufe(data);
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

    @GetMapping("/vorlage/{id}")
    public RechnungDTO getVorlage(@PathVariable final Integer id) {
        RechnungDTO dto = service.getRechnung(id);
        if (!dto.getRechnung().isVorlage()) {
            dto = service.getRechnung(-1);
        }
        if (dto.getRechnung().getId() != null) {
            protokollService.write(dto.getRechnung().getId(), RECHNUNG, String.valueOf(dto.getRechnung().getNummer()), ANGESEHEN);
        }
        return dto;
    }

    @GetMapping("/{id}/storno")
    public RechnungDTO getOhneStorno(@PathVariable final Integer id) {
        return service.getRechnung(id, true);
    }

    @GetMapping("/{id}/stornobeleg")
    public StornoDto getStornoBeleg(@PathVariable final Integer id) {
        return stornierungService.getStornobeleg(id);
    }

    @Transactional
    @PutMapping
    public Map<String, Object> save(@RequestBody final RechnungDTO dto) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(dto.getRechnung()));
        handleVorlage(dto);
        final Rechnung rechnung = dto.getRechnung();
        final boolean isErstellt = dto.getRechnung().getId() == null;
        polishRechnung(rechnung);
        final Zahlart zahlart = Zahlart.getByCode(rechnung.getArt());
        validateZahlart(rechnung, result, zahlart);
        for (final Rechnungsposten posten : dto.getPosten()) {
            result.putAll(validationService.validate(posten));
        }
        if (result.isEmpty()) {
            changeBezahltStatus(rechnung, isErstellt, zahlart);
            final Rechnung saved = service.saveRechnung(rechnung);
            savePosten(dto.getPosten(), saved);
            reparaturErledigen(saved);
            angebotErledigen(saved);
            bestellungErledigen(saved);
            if (isErstellt) {
                final List<Rechnungsposten> posten = dto.getPosten().stream()
                        .filter(p -> p.getProdukt() != null && p.getProdukt().getId() != null).collect(Collectors.toList());
                inventarAnpassen(posten);
                einkaufService.saveEinkaufsliste(posten);
            }
            if (isErstellt && rechnung.getKunde() != null && !rechnung.getKunde().isDsgvo()) {
                kundeService.saveDsgvo(rechnung.getKunde().getId());
                result.put(Message.INFO.getCode(), messageService.get("dsgvo.info"));
            } else {
                result.put(Message.SUCCESS.getCode(), messageService.get("rechnung.save.success", saved.getNummer()));
            }
            result.put("rechnung", saved);
            protokollService.write(saved.getId(), RECHNUNG, String.valueOf(rechnung.getNummer()), isErstellt ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    private void handleVorlage(final RechnungDTO dto) {
        if (dto.getRechnung().isVorlage()) {
            if (dto.isVorlageBehalten()) {
                saveVorlage(dto);
            } else {
                service.deleteRechnung(dto.getRechnung().getId());
            }
            dto.setRechnung(new Rechnung(dto.getRechnung()));
            final List<Rechnungsposten> posten = dto.getPosten().stream().map(Rechnungsposten::new).collect(Collectors.toList());
            dto.getPosten().clear();
            dto.setPosten(posten);
        }
    }

    @Transactional
    @PutMapping("/vorlage")
    public Map<String, Object> saveVorlage(@RequestBody final RechnungDTO dto) {
        final Rechnung rechnung = dto.getRechnung();
        final Map<String, Object> result = new HashMap<>(validationService.validate(rechnung));

        final boolean isErstellt = dto.getRechnung().getId() == null;
        polishRechnung(rechnung);
        final Zahlart zahlart = Zahlart.getByCode(rechnung.getArt());
        validateZahlart(rechnung, result, zahlart);
        for (final Rechnungsposten posten : dto.getPosten()) {
            result.putAll(validationService.validate(posten));
        }
        if (result.isEmpty()) {
            changeBezahltStatus(rechnung, isErstellt, zahlart);
            final Rechnung saved = service.saveRechnungsvorlage(rechnung);
            service.deletePosten(saved);
            dto.getPosten().forEach(p -> p.setId(null));
            savePosten(dto.getPosten(), saved);
            result.put(Message.SUCCESS.getCode(), messageService.get("rechnung.save.vorlage.success"));
            result.put("rechnung", saved);
            protokollService.write(saved.getId(), RECHNUNG, String.valueOf(rechnung.getNummer()), isErstellt ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    private void validateZahlart(final Rechnung rechnung, final Map<String, Object> result, final Zahlart zahlart) {
        if (zahlart == Zahlart.UEBERWEISUNG && relevanteFelderNichtGefuellt(rechnung.getKunde())) {
            result.put(Message.ERROR.getCode(), messageService.get("rechnung.kunde.error"));
        }
    }

    private void polishRechnung(final Rechnung rechnung) {
        if (rechnung.getBestellung() != null && rechnung.getBestellung().getId() == null) {
            rechnung.setBestellung(null);
        }
        if (rechnung.getReparatur() != null && rechnung.getReparatur().getId() == null) {
            rechnung.setReparatur(null);
        }
        if (rechnung.getAngebot() != null && rechnung.getAngebot().getId() == null) {
            rechnung.setAngebot(null);
        }
        if (rechnung.getKunde() != null && rechnung.getKunde().getId() == null) {
            rechnung.setKunde(null);
        }
    }

    private void changeBezahltStatus(final Rechnung rechnung, final boolean isErstellt, final Zahlart zahlart) {
        if (isErstellt) {
            rechnung.setBezahlt(zahlart != Zahlart.UEBERWEISUNG);
        } else {
            final Rechnung existing = service.getRechnung(rechnung.getId()).getRechnung();
            final Zahlart zahlartBefore = Zahlart.getByCode(existing.getArt());
            if (zahlartBefore != zahlart) {
                rechnung.setBezahlt(zahlart != Zahlart.UEBERWEISUNG);
            }
        }
    }

    private boolean relevanteFelderNichtGefuellt(final Kunde kunde) {
        boolean result = kunde == null;
        if (kunde != null) {
            result = StringUtils.isBlank(kunde.getNameKomplett()) || StringUtils.isBlank(kunde.getStrasse())
                    || StringUtils.isBlank(kunde.getPlz()) || StringUtils.isBlank(kunde.getOrt());
        }
        return result;
    }

    @PutMapping("/bezahlt")
    public Map<String, Object> rechnungBezahlt(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        if (obj.getId() == null) {
            result.put(Message.ERROR.getCode(), messageService.get("rechnung.save.error"));
        } else {
            final Rechnung rechnung = service.getRechnung(obj.getId()).getRechnung();
            if (rechnung.getId() != null) {
                final boolean bezahlt = !rechnung.isBezahlt();
                service.rechnungBezahlt(rechnung, bezahlt);
                protokollService.write(rechnung.getId(), REPARATUR,
                        messageService.get("protokoll.erledigt", rechnung.getNummer(), BooleanUtils.toStringYesNo(bezahlt)), GEAENDERT);
            }
            result.put(Message.SUCCESS.getCode(), messageService.get("rechnung.save.success", rechnung.getNummer()));
        }
        return result;
    }

    private void inventarAnpassen(final List<Rechnungsposten> posten) {
        inventarService.bestandReduzieren(posten);
    }

    private void savePosten(final List<Rechnungsposten> list, final Rechnung rechnung) {
        for (final Rechnungsposten posten : list) {
            posten.setRechnung(rechnung);
            service.savePosten(posten);
        }
    }

    private void angebotErledigen(final Rechnung saved) {
        if (saved.getAngebot() != null && saved.getAngebot().getId() != null) {
            final AngebotDto dto = angebotService.getAngebot(saved.getAngebot().getId());
            if (dto.getAngebot().getId() != null) {
                angebotService.angebotErledigen(dto.getAngebot(), true);
            }
        }
    }

    private void bestellungErledigen(final Rechnung saved) {
        if (saved.getBestellung() != null && saved.getBestellung().getId() != null) {
            final Optional<Bestellung> optional = bestellungService.getBestellung(saved.getBestellung().getId());
            if (optional.isPresent()) {
                bestellungService.bestellungErledigen(optional.get(), true);
            }
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
                messageService.get("rechnung.delete.success", dto.getRechnung().getNummer()));
    }

    @GetMapping("/monate")
    public List<DefaultKeyValue<Integer, String>> getMonate() {
        final List<DefaultKeyValue<Integer, String>> result = new ArrayList<>();

        final List<String> monate = Arrays.asList(new DateFormatSymbols(Locale.GERMANY).getMonths());
        for (int i = 0; i < monate.size() - 1; i++) {
            result.add(new DefaultKeyValue<>(i + 1, monate.get(i)));
        }
        return result;
    }

    @PutMapping("/export")
    public Map<String, Object> export(@RequestBody final RechnungExportDto dto) {
        final Map<String, Object> result = new HashMap<>();
        try {
            final int anzahl = exportService.export(dto);
            result.put(Message.SUCCESS.getCode(), messageService.get("rechnung.export.success", String.valueOf(anzahl)));
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
            result.put(Message.ERROR.getCode(), messageService.get("rechung.export.error", e.getMessage()));
        }
        return result;
    }

    @GetMapping("/offen/{kundeId}")
    public List<DefaultKeyValue<Integer, String>> hatKundeOffeneRechnungen(@PathVariable final Integer kundeId) {
        return service.hatKundeOffeneRechnungen(kundeId).stream()
                .map(r -> new DefaultKeyValue<>(r.getId(), r.getFiliale().getKuerzel() + r.getNummerAnzeige()))
                .collect(Collectors.toList());
    }

    @PostMapping("/storno")
    public Page<Stornierung> listStorno(@RequestBody final SearchData data) {
        return stornierungService.listStornierungen(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/storno/{id}")
    public StornoDto getStorno(@PathVariable final Integer id) {
        final StornoDto dto = stornierungService.getStorno(id);
        if (dto.getStorno().getId() != null) {
            protokollService.write(dto.getRechnung().getId(), STORNO, String.valueOf(dto.getRechnung().getNummer()), ANGESEHEN);
        }
        return dto;
    }

    @Transactional
    @PutMapping("/storno")
    public Map<String, Object> saveStorno(@RequestBody final StornoDto dto) {
        final Stornierung storno = dto.getStorno();
        final Map<String, Object> result = new HashMap<>(validationService.validate(storno));

        final boolean isErstellt = storno.getId() == null;
        if (storno.getKunde() != null && storno.getKunde().getId() == null) {
            storno.setKunde(null);
        }
        if (result.isEmpty()) {
            if (storno.isVollstorno()) {
                dto.setPosten(dto.getPosten().stream().map(p -> {
                    p.setStorno(true);
                    return p;
                }).collect(Collectors.toList()));
            } else {
                storno.setVollstorno(dto.getPosten().stream().allMatch(Rechnungsposten::isStorno));
            }
            final Stornierung saved = stornierungService.save(storno);
            stornierungService.savePosten(dto.getPosten(), saved);
            result.put(Message.SUCCESS.getCode(), messageService.get("rechnung.storno.save.success", saved.getNummer()));
            result.put("storno", saved);
            protokollService.write(saved.getId(), STORNO, String.valueOf(saved.getNummer()), isErstellt ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @DeleteMapping("/storno")
    public Map<String, Object> deleteStorno(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final StornoDto dto = stornierungService.getStorno(id);
        stornierungService.deleteStorno(id);
        if (dto.getStorno().getId() != null) {
            protokollService.write(dto.getRechnung().getId(), STORNO, String.valueOf(dto.getRechnung().getNummer()), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(),
                messageService.get("rechnung.storno.delete.success", dto.getRechnung().getNummer()));
    }

}
