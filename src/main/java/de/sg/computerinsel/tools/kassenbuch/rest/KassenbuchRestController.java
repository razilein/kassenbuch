package de.sg.computerinsel.tools.kassenbuch.rest;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.KASSENBERICHT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GELOESCHT;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuch;
import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuchposten;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchDTO;
import de.sg.computerinsel.tools.kassenbuch.service.KassenbuchService;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.ProtokollService;
import de.sg.computerinsel.tools.service.ValidationService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@RestController
@RequestMapping("/kassenbuch")
@Slf4j
public class KassenbuchRestController {

    @Autowired
    private KassenbuchService kassenbuchService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @Autowired
    private ProtokollService protokollService;

    @Autowired
    private ValidationService validationService;

    @PostMapping
    public Page<Kassenbuch> list(@RequestBody final SearchData data) {
        return kassenbuchService.listKassenbuch(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping
    public KassenbuchDTO initKassenbuch() {
        BigDecimal betrag;
        final String ausgangsbetrag = getAusgangsbetrag();
        try {
            betrag = new BigDecimal(ausgangsbetrag);
        } catch (final NumberFormatException | NullPointerException e) {
            log.debug(e.getMessage(), e);
            betrag = BigDecimal.ZERO;
        }
        return kassenbuchService.createKassenbuch(LocalDate.now(), betrag);
    }

    @DeleteMapping
    public Map<String, Object> deleteKunde(@RequestBody final Map<String, Object> data) {
        final int id = (int) data.get("id");

        final KassenbuchDTO dto = getKassenbuch(id);
        protokollService.write(dto.getKassenbuch().getId(), KASSENBERICHT, ProtokollService.getBezeichnungKassenbuch(dto), GELOESCHT);
        kassenbuchService.delete(id);
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("kassenbuch.delete.success"));
    }

    @GetMapping("/drucken/{id}")
    public KassenbuchDTO getKassenbuch(@PathVariable final Integer id) {
        return kassenbuchService.get(id);
    }

    @GetMapping("/{datum}")
    public List<Kassenbuchposten> getPosten(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate datum) {
        return kassenbuchService.listKassenbuchposten(datum);
    }

    @GetMapping("/ausgangsbetrag")
    public String getAusgangsbetrag() {
        final Optional<Filiale> filiale = mitarbeiterService.getAngemeldeterMitarbeiterFiliale();
        return filiale.isPresent() ? filiale.get().getAusgangsbetrag().toString() : null;
    }

    @PutMapping
    public Map<String, Object> erstelleKassenbuch(@RequestBody final KassenbuchDTO dto) {
        final Map<String, Object> result = new HashMap<>(validationService.validate(dto.getKassenbuch()));
        if (!result.containsKey(Message.ERROR.getCode())) {
            kassenbuchService.saveAusgangsbetrag(dto);
            result.put("kassenbuch", kassenbuchService.saveKassenbuch(dto));
            result.put(Message.SUCCESS.getCode(), messageService.get("kassenbuch.save.success"));
            protokollService.write(
                    messageService.get("protokoll.kassenbuch", dto.getKassenbuch().getDatum(), dto.getKassenbuch().getAusgangsbetrag()));
        }
        return result;
    }

}
