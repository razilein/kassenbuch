package de.sg.computerinsel.tools.reparatur.service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.kunde.service.EmailService;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReparaturAutomatischeEmail implements Processor {

    private final EmailService emailService;

    private final EinstellungenService einstellungenService;

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Reparatur reparatur = exchange.getIn().getBody(Reparatur.class);

        if (StringUtils.isNotBlank(reparatur.getKunde().getEmail())) {
            final File beilage = (File) exchange.getProperty(ReparaturBeilageProcessor.PROP_BEILAGE);
            emailService.sendeEmail(reparatur, beilage, einstellungenService.getRoboterEmail().getWert(),
                    emailService.getEingangMailText(reparatur));
        }
        final String titel = "Computer-Insel Tools - Automatische Benachrichtung - Neuer Reparaturauftrag "
                + reparatur.getFiliale().getKuerzel() + reparatur.getNummer();
        final String email = einstellungenService.getRoboterEmail().getWert();
        emailService.sendeInformationsmail(titel, getText(reparatur), email, email);
    }

    private String getText(final Reparatur reparatur) {
        final List<String> text = new ArrayList<>();
        text.add("Hallo,");
        text.add(StringUtils.EMPTY);
        text.add("der neue Reparaturauftrag wurde im System eingelesen: " + reparatur.getFiliale().getKuerzel() + reparatur.getNummer());
        text.add(StringUtils.EMPTY);
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            log.debug(e.getMessage(), e);
            hostName = "kasse";
        }
        text.add("Der Auftrag kann unter folgender Adresse aufgerufen werden: http://" + hostName + "/reparatur-drucken.html?id="
                + reparatur.getId());
        text.add(StringUtils.EMPTY);
        if (StringUtils.isBlank(reparatur.getGeraetepasswort())) {
            text.add("Es wurde kein Gerätepasswort angegeben.");
            text.add(StringUtils.EMPTY);
        }
        text.add(StringUtils.EMPTY);
        text.add("Viele Liebe Grüße von den Computer-Insel Tools");
        return text.stream().collect(Collectors.joining(System.lineSeparator()));
    }

}
