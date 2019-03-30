package de.sg.computerinsel.tools.bestellung.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class FtpService {

    private static final String LINE_SEPARATOR = "\n";

    private static final String BESTELLLISTE_TRENNZEICHEN = LINE_SEPARATOR + "- - - - - - - - - - - -" + LINE_SEPARATOR;

    /* @formatter:off */
    private static final String DEFAULT_BESTELLLISTE =
            "* = Rückstand! ============================================"
            + LINE_SEPARATOR
            + LINE_SEPARATOR
            + "========================================================"
            + LINE_SEPARATOR
            + LINE_SEPARATOR
            + "========================================================"
            + BESTELLLISTE_TRENNZEICHEN
            + "PILOT:"
            + BESTELLLISTE_TRENNZEICHEN
            + BESTELLLISTE_TRENNZEICHEN
            + "MAXCOM: "
            + BESTELLLISTE_TRENNZEICHEN
            + BESTELLLISTE_TRENNZEICHEN
            + "ActionEU:"
            + BESTELLLISTE_TRENNZEICHEN
            + BESTELLLISTE_TRENNZEICHEN
            + "Kosatec:"
            + BESTELLLISTE_TRENNZEICHEN
            + BESTELLLISTE_TRENNZEICHEN
            + "Leicke:"
            + BESTELLLISTE_TRENNZEICHEN
            + BESTELLLISTE_TRENNZEICHEN
            + "energy-Ink:"
            + BESTELLLISTE_TRENNZEICHEN;
    /* @formatter:on */

    private final EinstellungenService einstellungenService;

    public Optional<FTPClient> connect() {
        final FTPClient client = new FTPClient();
        client.setControlEncoding(StandardCharsets.UTF_8.name());
        client.setAutodetectUTF8(true);
        final String hostname = einstellungenService.getFtpHost().getWert();
        final String port = einstellungenService.getFtpPort().getWert();
        try {
            if (StringUtils.isNumeric(port)) {
                client.connect(hostname, Ints.tryParse(port));
            } else {
                client.connect(hostname);
            }
            client.enterLocalPassiveMode();
            if (client.login(einstellungenService.getFtpUser().getWert(), einstellungenService.getFtpPassword().getWert())) {
                return Optional.of(client);
            } else {
                return Optional.empty();
            }
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public String downloadBestellliste(final String dateiname) {
        final Optional<FTPClient> client = connect();
        if (client.isPresent()) {
            try {
                final InputStream stream = client.get().retrieveFileStream(dateiname);
                if (stream == null) {
                    return DEFAULT_BESTELLLISTE;
                }
                return IOUtils.toString(stream, StandardCharsets.ISO_8859_1);
            } catch (final IOException e) {
                log.error(e.getMessage(), e);
                return e.getMessage();
            }
        } else {
            return DEFAULT_BESTELLLISTE;
        }
    }

    public void uploadBestellliste(final String dateiname, final String text) {
        final Optional<FTPClient> client = connect();
        if (client.isPresent()) {
            final InputStream stream = IOUtils.toInputStream(text, StandardCharsets.ISO_8859_1);
            try {
                client.get().storeFile(dateiname, stream);
            } catch (final IOException e) {
                throw new IllegalStateException("Fehler bem Hochladen der Datei: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalStateException("Der FTP-Server ist nicht erreichbar.");
        }
    }

}
