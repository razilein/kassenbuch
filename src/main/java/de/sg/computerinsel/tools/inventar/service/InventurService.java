package de.sg.computerinsel.tools.inventar.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.CurrencyUtils;
import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.inventar.dao.ProduktRepository;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class InventurService {

    private static final String TRENNZEICHEN = "|";

    private final EinstellungenService einstellungenService;

    private final ProduktRepository produktRepository;

    public void starteInventur() {
        final List<Produkt> list = produktRepository.findByBestandGreaterThanAndBestandUnendlichOrderByBezeichnungAsc(0, false);

        BigDecimal betrag = BigDecimal.ZERO;
        BigDecimal betragGesamt = BigDecimal.ZERO;
        try (final FileWriter writer = new FileWriter(createFile())) {
            writeHeadline(writer);
            for (final Produkt produkt : list) {
                if (produkt.getPreisVkNetto() != null || produkt.getPreisEkNetto() != null) {
                    writeProduktToFile(writer, produkt);

                    final BigDecimal einzelpreis = getBetrag(produkt);
                    betrag = betrag.add(einzelpreis);
                    betragGesamt = betragGesamt.add(einzelpreis.multiply(new BigDecimal(produkt.getBestand())));
                }
            }
            writeFootline(writer, betrag, betragGesamt);
            writer.flush();
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void writeHeadline(final FileWriter writer) throws IOException {
        writer.write("Anzahl");
        writer.write(TRENNZEICHEN);
        writer.write("Produkt");
        writer.write(TRENNZEICHEN);
        writer.write("Einkaufspreis (netto)");
        writer.write(TRENNZEICHEN);
        writer.write("Gesamt Einkaufspreis (netto)");
        writer.write(System.lineSeparator());
    }

    private void writeProduktToFile(final FileWriter writer, final Produkt produkt) throws IOException {
        writer.write(String.valueOf(produkt.getBestand()));
        writer.write(TRENNZEICHEN);
        writer.write(produkt.getBezeichnung());
        writer.write(TRENNZEICHEN);
        final BigDecimal betrag = getBetrag(produkt);
        writer.write(CurrencyUtils.format(betrag));
        writer.write(TRENNZEICHEN);
        writer.write(CurrencyUtils.format(betrag.multiply(new BigDecimal(produkt.getBestand()))));
        writer.write(System.lineSeparator());
    }

    private void addEmptyLine(final FileWriter writer) throws IOException {
        writer.write(TRENNZEICHEN);
        writer.write(TRENNZEICHEN);
        writer.write(TRENNZEICHEN);
        writer.write(System.lineSeparator());
    }

    private void writeFootline(final FileWriter writer, final BigDecimal betrag, final BigDecimal betragGesamt) throws IOException {
        addEmptyLine(writer);
        writer.write(StringUtils.EMPTY);
        writer.write(TRENNZEICHEN);
        writer.write("Gesamt");
        writer.write(TRENNZEICHEN);
        writer.write(CurrencyUtils.format(betrag));
        writer.write(TRENNZEICHEN);
        writer.write(CurrencyUtils.format(betragGesamt));
        writer.write(System.lineSeparator());
    }

    private BigDecimal getBetrag(final Produkt produkt) {
        BigDecimal betrag = produkt.getPreisEkNetto();
        if (betrag == null && produkt.getPreisVkNetto() != null) {
            betrag = produkt.getPreisVkNetto().subtract(produkt.getPreisVkNetto().multiply(BigDecimal.TEN).divide(new BigDecimal("100")));
        }
        if (betrag == null) {
            betrag = BigDecimal.ZERO;
        }
        return betrag;
    }

    private File createFile() {
        final File ablageverzeichnis = new File(einstellungenService.getAblageverzeichnis().getWert());
        return new File(ablageverzeichnis, DateUtils.nowDatetime() + "_inventur.xls");
    }

}
