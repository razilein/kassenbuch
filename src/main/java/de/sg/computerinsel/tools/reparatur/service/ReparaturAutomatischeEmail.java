package de.sg.computerinsel.tools.reparatur.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.kunde.service.EmailService;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReparaturAutomatischeEmail implements Processor {

    private final EmailService emailService;

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Reparatur reparatur = exchange.getIn().getBody(Reparatur.class);

        if (StringUtils.isNotBlank(reparatur.getKunde().getEmail())) {
            emailService.sendeEmail(reparatur, emailService.getEingangMailText(reparatur));
        }

    }

}
