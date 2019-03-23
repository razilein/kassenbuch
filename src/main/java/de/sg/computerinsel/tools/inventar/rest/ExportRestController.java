package de.sg.computerinsel.tools.inventar.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.inventar.model.ProduktDTO;
import de.sg.computerinsel.tools.inventar.service.ExportImportService;
import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.service.ProtokollService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/inventar/export")
@Slf4j
public class ExportRestController {

    @Autowired
    private InventarService service;

    @Autowired
    private ExportImportService exportImportService;

    @Autowired
    private ProtokollService protokollService;

    @PostMapping
    public List<Produkt> list(@RequestBody final Map<String, LocalDate> conditions) {
        return service.listProdukteInAenderungszeitraum(conditions);
    }

    @PutMapping
    public Map<String, Object> export(@RequestBody final List<ProduktDTO> produkte) {
        try {
            exportImportService.export(produkte.stream().filter(p -> StringUtils.isNotBlank(p.getEan())).collect(Collectors.toList()));
            protokollService.write("Export Produkte. Anzahl: " + produkte.size());
        } catch (final IOException e) {
            log.debug(e.getMessage(), e);
            return Collections.singletonMap(Message.ERROR.getCode(),
                    "Beim Schreiben der Exportdatei ist ein Fehler aufgetreten: " + e.getMessage());
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(),
                "Die Produkte wurden erfolgreich exportiert und im Ablageverzeichnis abgelegt.");
    }

}
