package de.sg.computerinsel.tools.inventar.service;

import static de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle.PRODUKT;
import static de.sg.computerinsel.tools.model.Protokoll.Protokolltyp.GEAENDERT;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.inventar.dao.ProduktRepository;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.inventar.model.ProduktDTO;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.ProtokollService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class ExportImportService {

    private static final String EXPORT_FILENAME_JSON = "export_produkte.json";

    private final EinstellungenService einstellungenService;

    private final ProduktRepository produktRepository;

    private final ProtokollService protokollService;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class ExportProdukt {
        List<ProduktDTO> produkte;
    }

    public void export(final List<ProduktDTO> produkte) throws IOException {
        final File file = new File(einstellungenService.getAblageverzeichnis().getWert(), DateUtils.nowDatetime() + EXPORT_FILENAME_JSON);
        new ObjectMapper().writeValue(file, new ExportProdukt(produkte));
    }

    public List<ProduktDTO> getProdukteFromFile(final MultipartFile file) throws IOException {
        return new ObjectMapper().readValue(file.getBytes(), ExportProdukt.class).getProdukte();
    }

    public void importProdukte(final List<ProduktDTO> produkte) {
        for (final ProduktDTO dto : produkte) {
            final List<Produkt> produkteByEan = produktRepository.findByEan(dto.getEan());
            if (produkteByEan.isEmpty()) {
                log.debug("Keine Produkte mit EAN '{}' gefunden.", dto.getEan());
            } else if (produkteByEan.size() > 1) {
                log.debug("Es wurden {} Produkte mit der EAN '{}' gefunden. Eine Übernahme ist nicht möglich", produkteByEan.size(),
                        dto.getEan());
            } else {
                final Produkt produkt = produkteByEan.get(0);
                produkt.setBezeichnung(dto.getBezeichnung());
                produkt.setBestandUnendlich(dto.isBestandUnendlich());
                produkt.setPreisEkBrutto(dto.getPreisEkBrutto());
                produkt.setPreisEkNetto(dto.getPreisEkNetto());
                produkt.setPreisVkBrutto(dto.getPreisVkBrutto());
                produkt.setPreisVkNetto(dto.getPreisVkNetto());
                produktRepository.save(produkt);
                protokollService.write(produkt.getId(), PRODUKT, produkt.getBezeichnung(), GEAENDERT);
            }

        }

    }

}
