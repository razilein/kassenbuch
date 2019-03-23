package de.sg.computerinsel.tools.inventar.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.sg.computerinsel.tools.inventar.model.ProduktDTO;
import de.sg.computerinsel.tools.inventar.service.ExportImportService;
import de.sg.computerinsel.tools.rest.Message;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/inventar/import")
@Slf4j
public class ImportRestController {

    @Autowired
    private ExportImportService service;

    @PostMapping
    public @ResponseBody List<ProduktDTO> list(@RequestParam("file") final MultipartFile file) {
        try {
            return service.getProdukteFromFile(file);
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @PutMapping
    public Map<String, Object> export(@RequestBody final List<ProduktDTO> produkte) {
        service.importProdukte(produkte);
        return Collections.singletonMap(Message.SUCCESS.getCode(), "Die Produkte wurden erfolgreich importiert.");
    }

}
