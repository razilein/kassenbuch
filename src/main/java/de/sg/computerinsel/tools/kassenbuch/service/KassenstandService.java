package de.sg.computerinsel.tools.kassenbuch.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.CurrencyUtils;
import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.kassenbuch.rest.model.Kassenstand;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Service
@Slf4j
@AllArgsConstructor
public class KassenstandService {

    private static final String TRENNZEICHEN = "|";

    private static final String FILENAME_PDF = "kassenstand.csv";

    private final EinstellungenService einstellungenService;

    public void ablegen(final List<Kassenstand> kassenstaende) {
        final File csvFile = new File(einstellungenService.getAblageverzeichnis().getWert(), DateUtils.nowDatetime() + FILENAME_PDF);
        try {
            if (csvFile.createNewFile()) {
                log.debug("Neue CSV-Datei erzeugt: {}", csvFile.getAbsolutePath());
            }
            final FileWriter writer = new FileWriter(csvFile.getAbsoluteFile());
            writeToFile(writer, kassenstaende);
            writer.flush();
            writer.close();
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void writeToFile(final FileWriter writer, final List<Kassenstand> kassenstaende) throws IOException {
        writeHeadline(writer);
        writeContentLine(writer, kassenstaende);
    }

    private void writeHeadline(final FileWriter writer) throws IOException {
        writer.write("Anzahl");
        writer.write(TRENNZEICHEN);
        writer.write("Wert");
        writer.write(TRENNZEICHEN);
        writer.write("Betrag");
        writer.write("\n\r");
    }

    private void writeContentLine(final FileWriter writer, final List<Kassenstand> kassenstaende) throws IOException {
        for (final Kassenstand kassenstand : kassenstaende) {
            writer.write(String.valueOf(kassenstand.getAnzahl()));
            writer.write(TRENNZEICHEN);
            writer.write(CurrencyUtils.format(kassenstand.getMulti()));
            writer.write(TRENNZEICHEN);
            writer.write(CurrencyUtils.format(kassenstand.getBetrag()));
            writer.write(TRENNZEICHEN);
            writer.write("\n\r");
        }

    }

}
