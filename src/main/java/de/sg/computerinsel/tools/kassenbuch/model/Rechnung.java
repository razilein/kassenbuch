package de.sg.computerinsel.tools.kassenbuch.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.DateUtils;
import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
public class Rechnung {

    public static final String AUSGANGSBETRAG = "Ausgangsbetrag";

    public static final String GESAMTBETRAG = "Gesamtbetrag";

    private String rechnungsnummer;

    private Date rechnungsdatum;

    private BigDecimal rechnungsbetrag;

    private Zahlart art;

    private List<Rechnungsposten> posten;

    private String adressfeld;

    public Date getRechnungsjahr() {
        return DateUtils.getYear(rechnungsdatum);
    }

    public Month getRechnungsmonat() {
        return rechnungsdatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth();
    }

    public LocalDate getRechnungsdatumWithoutTime() {
        return rechnungsdatum == null ? null : DateUtils.convert(rechnungsdatum);
    }

    public Integer getRechnungsnummerAsInt() {
        Integer nummer = 0;
        if (StringUtils.isNumeric(rechnungsnummer)) {
            nummer = Integer.valueOf(rechnungsnummer);
        } else if (rechnungsnummer != null) {
            nummer = Ints.tryParse(RegExUtils.replaceAll(rechnungsnummer, "[^\\d.]", StringUtils.EMPTY));
        }
        return nummer;
    }

    public String toCsvString(final BigDecimal gesamtBetrag) {
        final String formattedDate = rechnungsdatum == null ? StringUtils.EMPTY : DateUtils.format(rechnungsdatum);
        final String verwendungszweck = StringUtils.isNumeric(rechnungsnummer)
                || StringUtils.isNumeric(StringUtils.substring(rechnungsnummer, 1)) ? "Rechnung: " + rechnungsnummer : rechnungsnummer;
        final String formattedRechnungsbetrag = new DecimalFormat("#0.00").format(rechnungsbetrag);

        return Joiner.on(";").join(formattedDate, verwendungszweck,
                StringUtils.startsWith(formattedRechnungsbetrag, "-") ? "" : formattedRechnungsbetrag,
                !StringUtils.startsWith(formattedRechnungsbetrag, "-") ? "" : formattedRechnungsbetrag, gesamtBetrag, "\r\n");
    }

}
