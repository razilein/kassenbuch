package de.sg.computerinsel.tools.einkauf.service;

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
import de.sg.computerinsel.tools.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class FtpService {

    private static final String LINE_SEPARATOR = "\n";

    private static final String EINKAUFSLISTE_TRENNZEICHEN = LINE_SEPARATOR + "- - - - - - - - - - - -" + LINE_SEPARATOR;

    /* @formatter:off */
    private static final String DEFAULT_EINKAUFSLISTE =
            "* = Rückstand! ============================================"
            + LINE_SEPARATOR
            + LINE_SEPARATOR
            + "========================================================"
            + LINE_SEPARATOR
            + LINE_SEPARATOR
            + "========================================================"
            + EINKAUFSLISTE_TRENNZEICHEN
            + "PILOT:"
            + EINKAUFSLISTE_TRENNZEICHEN
            + EINKAUFSLISTE_TRENNZEICHEN
            + "MAXCOM: "
            + EINKAUFSLISTE_TRENNZEICHEN
            + EINKAUFSLISTE_TRENNZEICHEN
            + "ActionEU:"
            + EINKAUFSLISTE_TRENNZEICHEN
            + EINKAUFSLISTE_TRENNZEICHEN
            + "Kosatec:"
            + EINKAUFSLISTE_TRENNZEICHEN
            + EINKAUFSLISTE_TRENNZEICHEN
            + "Leicke:"
            + EINKAUFSLISTE_TRENNZEICHEN
            + EINKAUFSLISTE_TRENNZEICHEN
            + "energy-Ink:"
            + EINKAUFSLISTE_TRENNZEICHEN;
    /* @formatter:on */

    private final EinstellungenService einstellungenService;

    private final MessageService messageService;

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

    public String downloadEinkaufsliste(final String dateiname) {
        final Optional<FTPClient> client = connect();
        if (client.isPresent()) {
            try {
                final InputStream stream = client.get().retrieveFileStream(dateiname);
                if (stream == null) {
                    return DEFAULT_EINKAUFSLISTE;
                }
                return IOUtils.toString(stream, StandardCharsets.ISO_8859_1);
            } catch (final IOException e) {
                log.error(e.getMessage(), e);
                return e.getMessage();
            }
        } else {
            return DEFAULT_EINKAUFSLISTE;
        }
    }

    public void uploadEinkaufsliste(final String dateiname, final String text) {
        final Optional<FTPClient> client = connect();
        if (client.isPresent()) {
            final InputStream stream = IOUtils.toInputStream(text, StandardCharsets.ISO_8859_1);
            try {
                client.get().storeFile(dateiname, stream);
            } catch (final IOException e) {
                throw new IllegalStateException(messageService.get("einkaufsliste.put.error.upload", e.getMessage()), e);
            }
        } else {
            throw new IllegalStateException(messageService.get("einkaufsliste.put.error.unavailable"));
        }
    }

}
