package de.sg.computerinsel.tools.reparatur.service;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReparaturRoboterRoute extends RouteBuilder {

    private final EinstellungenService einstellungService;

    @Override
    public void configure() throws Exception {
        final File file = new File(einstellungService.getAblageverzeichnisReparaturen());

        /* @formatter:off */
        onException(Exception.class)
          .handled(true)
          .bean(ReparaturRoboterFehler.class);

        from("file://" + file.getAbsolutePath() + "?moveFailed=.error&move=.done&include=.*.csv")
          .transacted()
          .bean(DateiZuReparaturProcessor.class)
          .bean(ReparaturSpeichern.class)
          .bean(ReparaturAutomatischeEmail.class);
        /* @formatter:on */

    }

}
