package de.sg.computerinsel.tools.reparatur.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.service.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@RequiredArgsConstructor
@Service
public class ReparaturBeilageProcessor implements Processor {

    public static final String PROP_BEILAGE = "BEILAGE";

    private final ReportService reportService;

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Reparatur reparatur = exchange.getIn().getBody(Reparatur.class);

        try {
            final File beilage = reportService.createReportReparaturBeilage(reparatur);
            exchange.setProperty(PROP_BEILAGE, beilage);
        } catch (JRException | IOException | SQLException e) {
            throw new IllegalStateException("Fehler beim Erzeugen der Paketbeilage :" + e.getMessage(), e);
        }
        exchange.getMessage().setBody(reparatur);
    }

}
