package de.sg.computerinsel.tools.angebot.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.ANGEBOT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ANGESEHEN;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.ERSTELLT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

import de.sg.computerinsel.tools.angebot.dto.AngebotDto;
import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.angebot.model.Angebotsposten;
import de.sg.computerinsel.tools.angebot.model.VAngebot;
import de.sg.computerinsel.tools.angebot.service.AngebotService;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.model.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;

@RestController
@RequestMapping("/angebot")
public class AngebotRestController {

    @Autowired
    private KundeService kundeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private AngebotService service;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<VAngebot> list(@RequestBody final SearchData data) {
        return service.listAngebote(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping("/{id}")
    public AngebotDto getAngebot(@PathVariable final Integer id) {
        final AngebotDto dto = service.getAngebot(id);
        if (dto.getAngebot().getId() != null) {
            protokollService.write(dto.getAngebot().getId(), ANGEBOT, String.valueOf(dto.getAngebot().getNummer()), ANGESEHEN);
        }
        return dto;
    }

    @PutMapping
    @Transactional
    public Map<String, Object> saveAngebot(@RequestBody final AngebotDto dto) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(dto));
        if (result.isEmpty()) {
            final Angebot angebot = dto.getAngebot();
            final boolean isErstellen = angebot.getId() == null;
            final Angebot saved = service.save(angebot);
            dto.getAngebotsposten().forEach(p -> p.setAngebot(saved));
            service.savePosten(dto.getAngebotsposten());
            if (!isErstellen) {
                // Gelöschte Posten entfernen
                final Set<Integer> neuePostenIds = dto.getAngebotsposten().stream().filter(p -> p.getId() != null)
                        .map(Angebotsposten::getId).collect(Collectors.toSet());
                service.getAngebotsposten(saved.getId()).stream().map(Angebotsposten::getId).filter(id -> !neuePostenIds.contains(id))
                        .forEach(service::deleteAngebotsposten);
            }
            if (isErstellen && angebot.getKunde() != null && !angebot.getKunde().isDsgvo()) {
                kundeService.saveDsgvo(angebot.getKunde().getId());
                result.put(Message.INFO.getCode(), messageService.get("dsgvo.info"));
            } else {
                result.put(Message.SUCCESS.getCode(), messageService.get("angebot.save.success", angebot.getNummer()));
            }
            result.put("angebot", saved);
            protokollService.write(saved.getId(), ANGEBOT, String.valueOf(angebot.getNummer()), isErstellen ? ERSTELLT : GEAENDERT);
        }
        return result;
    }

    @PutMapping("/erledigen")
    public Map<String, Object> angebotErledigen(@RequestBody final IntegerBaseObject obj) {
        final Map<String, Object> result = new HashMap<>();
        if (obj.getId() == null) {
            result.put(Message.ERROR.getCode(), messageService.get("angebot.save.error"));
        } else {
            final AngebotDto dto = service.getAngebot(obj.getId());
            if (dto.getAngebot().getId() != null) {
                final Angebot angebot = dto.getAngebot();
                final boolean erledigt = !angebot.isErledigt();
                service.angebotErledigen(angebot, erledigt);
                protokollService.write(angebot.getId(), ANGEBOT,
                        messageService.get("protokoll.erledigt", String.valueOf(angebot.getNummer()), BooleanUtils.toStringYesNo(erledigt)),
                        GEAENDERT);
                result.put(Message.SUCCESS.getCode(), messageService.get("angebot.save.success", angebot.getNummer()));
            }
        }
        return result;
    }

    @DeleteMapping
    public Map<String, Object> deleteAngebot(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");
        final AngebotDto dto = service.getAngebot(id);
        final Angebot angebot = dto.getAngebot();
        if (angebot.getId() != null) {
            service.deleteAngebot(angebot.getId());
            protokollService.write(angebot.getId(), ANGEBOT, String.valueOf(angebot.getNummer()), GELOESCHT);
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("angebot.delete.success"));
    }

}
