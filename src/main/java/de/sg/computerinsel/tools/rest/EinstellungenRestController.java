package de.sg.computerinsel.tools.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.FILIALE;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.MITARBEITER;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.RECHTE;
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

import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.model.EinstellungenData;
import de.sg.computerinsel.tools.rest.model.FilialeDto;
import de.sg.computerinsel.tools.rest.model.MitarbeiterDTO;
import de.sg.computerinsel.tools.rest.model.MitarbeiterRollenDTO;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/einstellungen")
@Slf4j
public class EinstellungenRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private ValidationService validationService;

    @GetMapping
    public EinstellungenData getEinstellungen() {
        final EinstellungenData data = new EinstellungenData();
        data.setAblageverzeichnis(einstellungenService.getAblageverzeichnis());
        data.setFtpHost(einstellungenService.getFtpHost());
        data.setFtpPort(einstellungenService.getFtpPort());
        data.setFtpUser(einstellungenService.getFtpUser());
        data.setFtpPassword(einstellungenService.getFtpPassword());
        data.setSmtpHost(einstellungenService.getSmtpHost());
        data.setSmtpPort(einstellungenService.getSmtpPort());
        data.setSmtpUser(einstellungenService.getSmtpUser());
        data.setSmtpPassword(einstellungenService.getSmtpPassword());
        data.setMailSignatur(einstellungenService.getMailSignatur());
        data.setMailBodyRechnung(einstellungenService.getMailBodyRechnung());
        data.setMailBodyReparatur(einstellungenService.getMailBodyReparaturauftrag());
        return data;
    }

    @GetMapping("/standardfiliale-mitarbeiter")
    public Filiale getStandardFilialeMitarbeiter() {
        return mitarbeiterService.getAngemeldeterMitarbeiterFiliale().orElseGet(Filiale::new);
    }

    @PutMapping
    public Map<String, Object> saveEinstellungen(@RequestBody final EinstellungenData data) {
        final Map<String, Object> result = new HashMap<>();
        result.putAll(ValidationUtils.validateVerzeichnisse(data.getAblageverzeichnis().getWert()));

        if (result.isEmpty()) {
            einstellungenService.save(data.getAblageverzeichnis());
            einstellungenService.save(data.getFtpHost());
            einstellungenService.save(data.getFtpPort());
            einstellungenService.save(data.getFtpUser());
            einstellungenService.save(data.getFtpPassword());
            einstellungenService.save(data.getSmtpHost());
            einstellungenService.save(data.getSmtpPort());
            einstellungenService.save(data.getSmtpUser());
            einstellungenService.save(data.getSmtpPassword());
            einstellungenService.save(data.getMailSignatur());
            einstellungenService.save(data.getMailBodyRechnung());
            einstellungenService.save(data.getMailBodyReparatur());
            result.put(Message.SUCCESS.getCode(), messageService.get("einstellungen.save.success"));
            protokollService.write(messageService.get("protokoll.einstellungen.save"));
        }

        return result;
    }

    @GetMapping("/filiale")
    public List<DefaultKeyValue<Integer, String>> getFilialen() {
        return einstellungenService.listFiliale();
    }

    @PostMapping("/filiale")
    public Page<Filiale> getFilialen(@RequestBody final SearchData data) {
        return einstellungenService.listFiliale(data.getData().getPagination());
    }

    @GetMapping("/filiale/{id}")
    public FilialeDto getFiliale(@PathVariable final Integer id) {
        final FilialeDto dto = einstellungenService.getFilialeDto(id);
        if (dto.getFiliale().getId() != null) {
            protokollService.write(id, FILIALE, dto.getFiliale().getName(), ANGESEHEN);
        }
        return dto;
    }

    @PutMapping("/filiale")
    public Map<String, Object> saveFiliale(@RequestBody final FilialeDto filiale) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(filiale));
        if (result.isEmpty()) {
            final FilialeDto saved = einstellungenService.save(filiale);
            protokollService.write(saved.getFiliale().getId(), FILIALE, saved.getFiliale().getName(),
                    filiale.getFiliale().getId() == null ? ERSTELLT : GEAENDERT);
            result.put(Message.SUCCESS.getCode(), messageService.get("einstellungen.filiale.save.success", filiale.getFiliale().getName()));
        }
        return result;
    }

    @PostMapping("/mitarbeiter")
    public Page<MitarbeiterDTO> getMitarbeiter(@RequestBody final SearchData data) {
        return einstellungenService.listMitarbeiter(data.getData().getPagination());
    }

    @GetMapping("/mitarbeiter/{id}")
    public MitarbeiterDTO getMitarbeiter(@PathVariable final Integer id) {
        final Optional<Mitarbeiter> optional = einstellungenService.getMitarbeiter(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), MITARBEITER, optional.get().getCompleteName(), ANGESEHEN);
        }
        return optional.map(MitarbeiterDTO::new).orElseGet(EinstellungenRestController::createMitarbeiter);
    }

    private static MitarbeiterDTO createMitarbeiter() {
        final MitarbeiterDTO dto = new MitarbeiterDTO();
        dto.setFiliale(new Filiale());
        return dto;
    }

    @PutMapping("/mitarbeiter")
    public Map<String, Object> saveMitarbeiter(@RequestBody final MitarbeiterDTO dto) {
        final Optional<Mitarbeiter> optional = dto.getId() == null ? Optional.of(new Mitarbeiter(dto))
                : einstellungenService.getMitarbeiter(dto.getId());
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), MITARBEITER,
                    messageService.get("protokoll.einstellungen.profildaten.save", optional.get().getCompleteName()),
                    dto.getId() == null ? ERSTELLT : GEAENDERT);
        }
        return mitarbeiterService.saveMitarbeiterProfil(dto, optional);
    }

    @DeleteMapping("/mitarbeiter")
    public Map<String, Object> deleteMitarbeiter(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final Optional<Mitarbeiter> optional = einstellungenService.getMitarbeiter(id);
        einstellungenService.deleteMitarbeiter(id);
        if (optional.isPresent()) {
            protokollService.write(optional.get().getId(), MITARBEITER, optional.get().getCompleteName(), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("einstellungen.mitarbeiter.delete.success"));
    }

    @PutMapping("/mitarbeiter/reset")
    public Map<String, Object> resetMitarbeiter(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        final Optional<Mitarbeiter> optional = einstellungenService.getMitarbeiter(obj.getId());
        if (optional.isPresent()) {
            final Mitarbeiter mitarbeiter = optional.get();
            mitarbeiter.setPasswort(null);
            einstellungenService.save(mitarbeiter);
            protokollService.write(mitarbeiter.getId(), MITARBEITER,
                    messageService.get("protokoll.einstellungen.mitarbeiter.psw.reset", optional.get().getCompleteName()), GEAENDERT);
            result.put(Message.SUCCESS.getCode(), messageService.get("einstellungen.mitarbeiter.psw.reset.success"));
            log.debug("Passwort für Benutzer '{}' zurückgesetzt.", mitarbeiter.getBenutzername());
        } else {
            result.put(Message.ERROR.getCode(), messageService.get("einstellungen.mitarbeiter.psw.reset.error"));
            log.debug("Benutzer mit ID '{}' nicht gefunden.", obj.getId());
        }
        return result;
    }

    @GetMapping("/mitarbeiter/rechte/{id}")
    public MitarbeiterRollenDTO getMitarbeiterRollen(@PathVariable final Integer id) {
        protokollService.write(id, RECHTE, ANGESEHEN);
        return mitarbeiterService.listRollen(id);
    }

    @PutMapping("/mitarbeiter/rechte")
    public Map<String, Object> saveMitarbeiterRollen(@RequestBody final MitarbeiterRollenDTO dto) {
        final Map<String, Object> result = new HashMap<>();
        mitarbeiterService.saveRollen(dto);
        protokollService.write(dto.getMitarbeiterId(), RECHTE, GEAENDERT);
        result.put(Message.SUCCESS.getCode(), messageService.get("einstellungen.mitarbeiter.rechte.save.success"));
        return result;
    }

}
