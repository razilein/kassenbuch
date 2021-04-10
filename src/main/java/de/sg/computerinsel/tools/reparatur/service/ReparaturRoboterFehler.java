package de.sg.computerinsel.tools.reparatur.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.kunde.service.EmailService;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReparaturRoboterFehler implements Processor {

    private final EinstellungenService einstellungenService;

    private final EmailService emailService;

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.error(exception.getMessage(), exception);

        final String titel = "Computer-Insel Tools - Automatische Benachrichtung - Ein Reparaturauftrag konnte wegen eines Fehlers nicht eingelesen werden";
        final String email = einstellungenService.getRoboterEmail().getWert();
        emailService.sendeInformationsmail(titel, getText(exception), email, email);
    }

    private String getText(final Exception exception) {
        final List<String> text = new ArrayList<>();
        text.add("Hallo,");
        text.add(StringUtils.EMPTY);
        text.add("beim automatischen Verarbeiten eines Reparaturauftrages ist dieser Fehler aufgetreten: ");
        text.add(exception.getMessage());
        text.add(StringUtils.EMPTY);
        text.add("Die Datei wurde in diesem Verzeichnis abgelegt:");
        text.add(einstellungenService.getAblageverzeichnisReparaturen() + File.separator + ".error");
        text.add(StringUtils.EMPTY);
        text.add(
                "Bitte prüfen und korrigieren Sie die Datei. Sollte keine Korrektur möglich sein, muss der Auftrag manuell erfasst und die Datei aus dem Verzeichnis gelöscht werden.");
        text.add(StringUtils.EMPTY);
        text.add("Viele Liebe Grüße von den Computer-Insel Tools");
        return text.stream().collect(Collectors.joining(System.lineSeparator()));
    }

}
