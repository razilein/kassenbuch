package de.sg.computerinsel.tools.kassenbuch.rest.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import de.sg.computerinsel.tools.DateUtils;
import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
public class KassenbuchErstellenData {

    @NotNull(message = "Bitte geben Sie den Rechnungszeitraum von an.")
    private Date zeitraumVon;

    @NotNull(message = "Bitte geben Sie den Rechnungszeitraum bis an.")
    private Date zeitraumBis;

    @NotNull(message = "Bitte geben Sie einen Ausgangsbetrag an.")
    @DecimalMin(message = "Der Betrag muss mindestens 0,01 EUR betragen.", value = "0.01")
    private BigDecimal ausgangsbetrag;

    @NotNull(message = "Bitte geben Sie an für welches Datum der Ausgangsbetrag gelten soll.")
    private Date ausgangsbetragDatum;

    public Date getZeitraumVon() {
        // remove time information
        final LocalDate date = DateUtils.convert(zeitraumVon);
        return DateUtils.convert(date);
    }

    public Date getZeitraumBis() {
        // remove time information
        final LocalDate date = DateUtils.convert(zeitraumBis);
        return DateUtils.convert(date);
    }

}
