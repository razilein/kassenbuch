package de.sg.computerinsel.tools.kassenbuch.rest.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
public class KassenbuchEintragungManuell {

    public static final BigDecimal IST_NEGATIVE_EINTRAGUNGSART = new BigDecimal("-1");

    @NotNull(message = "Bitte geben Sie einen Verwendungszweck an.")
    private String verwendungszweck;

    @NotNull(message = "Bitte geben Sie einen Betrag ein.")
    private BigDecimal betrag;

    @NotNull(message = "Bitte geben Sie das Datum der Eintragung an.")
    private Date datum;

    private BigDecimal eintragungsart = IST_NEGATIVE_EINTRAGUNGSART;

    private boolean gespeichert;

}
