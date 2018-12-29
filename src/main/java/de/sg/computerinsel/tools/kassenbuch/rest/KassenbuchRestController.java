package de.sg.computerinsel.tools.kassenbuch.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.ProtokollService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@RestController
@RequestMapping("/kassenbuch")
@Slf4j
public class KassenbuchRestController {

    @Autowired
    private EinstellungenService einstellungenService;

    @Autowired
    private KassenbuchService kassenbuchService;

    @Autowired
    private ProtokollService protokollService;

    @PostMapping
    public Page<Kassenbuch> list(@RequestBody final SearchData data) {
        return kassenbuchService.listKassenbuch(data.getData().getPagination(), data.getConditions());
    }

    @GetMapping
    public KassenbuchDTO initKassenbuch() {
        BigDecimal betrag;
        final String ausgangsbetrag = einstellungenService.getAusgangsbetrag().getWert();
        try {
            betrag = new BigDecimal(ausgangsbetrag);
        } catch (final NumberFormatException e) {
            log.debug(e.getMessage(), e);
            betrag = BigDecimal.ZERO;
        }
        return kassenbuchService.createKassenbuch(LocalDate.now(), betrag);
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
        return einstellungenService.getAusgangsbetrag().getWert();
    }

    @PutMapping
    public Map<String, Object> erstelleKassenbuch(@RequestBody final KassenbuchDTO dto) {
        final Map<String, Object> result = new HashMap<>(ValidationUtils.validate(dto.getKassenbuch()));
        if (!result.containsKey(Message.ERROR.getCode())) {
            kassenbuchService.saveAusgangsbetrag(dto);
            result.put("kassenbuch", kassenbuchService.saveKassenbuch(dto));
            result.put(Message.SUCCESS.getCode(), "Das Kassenbuch wurde erfolgreich erstellt.");
            protokollService.write("Kassenbuch erstellt: " + dto.getKassenbuch().getDatum() + " mit Ausgangsbetrag: "
                    + dto.getKassenbuch().getAusgangsbetrag());
        }
        return result;
    }

}
