package de.sg.computerinsel.tools.reparatur.service;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReparaturSpeichern implements Processor {

    private final ReparaturService reparaturService;

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Reparatur reparatur = exchange.getIn().getBody(Reparatur.class);
        exchange.getMessage().setBody(reparaturService.save(reparatur));
    }

}
