package de.sg.computerinsel.tools.kassenbuch.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchStatistik;
import de.sg.computerinsel.tools.kassenbuch.service.StatistikService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.service.ProtokollService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@RestController
@Slf4j
@RequestMapping("/kassenbuch/statistiken")
public class StatistikenRestController {

    @Autowired
    private StatistikService service;

    @Autowired
    private ProtokollService protokollService;

    @PutMapping("/zahlungsarten")
    public Map<String, Object> erstellenZahlungsarten(@RequestBody final KassenbuchStatistik statistik) {
        final Map<String, Object> result = ValidationUtils.validate(statistik);
        if (result.isEmpty()) {
            try {
                service.erstellenZahlungsarten(statistik);
                protokollService
                        .write("Statistik nach Zahlungsarten erstellt: " + statistik.getZeitraumVon() + " - " + statistik.getZeitraumBis());
                result.put(Message.SUCCESS.getCode(), "Die Statistik wurde erfolgreich erstellt und im Ablageverzeichnis hinterlegt.");
            } catch (final IllegalArgumentException e) {
                log.error(e.getMessage(), e);
                result.put(Message.ERROR.getCode(), e.getMessage());
            }
        }
        return result;
    }

    @PutMapping("/ueberweisungen")
    public Map<String, Object> erstellenUeberweisungen(@RequestBody final KassenbuchStatistik statistik) {
        final Map<String, Object> result = ValidationUtils.validate(statistik);
        if (result.isEmpty()) {
            service.erstellenUeberweisungen(statistik);
            protokollService
                    .write("Statistik nach Überweisungen erstellt: " + statistik.getZeitraumVon() + " - " + statistik.getZeitraumBis());
            result.put(Message.SUCCESS.getCode(), "Die Statistik wurde erfolgreich erstellt und im Ablageverzeichnis hinterlegt.");
        }
        return result;
    }

}
