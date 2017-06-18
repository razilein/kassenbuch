package de.sg.computerinsel.tools.kassenbuch.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.kassenbuch.KassenbuchErstellenUtils;
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

    public Date getRechnungsjahr() {
        return DateUtils.truncate(rechnungsdatum, Calendar.YEAR);
    }

    public Month getRechnungsmonat() {
        return rechnungsdatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth();
    }

    public Integer getRechnungsnummerAsInt() {
        return StringUtils.isNumeric(rechnungsnummer) ? Integer.valueOf(rechnungsnummer)
                : Ints.tryParse(StringUtils.substring(rechnungsnummer, 1));
    }

    public String toCsvString(final BigDecimal gesamtBetrag) {
        final String formattedDate = rechnungsdatum == null ? StringUtils.EMPTY
                : KassenbuchErstellenUtils.DATE_FORMAT.format(rechnungsdatum);
        final String verwendungszweck = StringUtils.isNumeric(rechnungsnummer)
                || StringUtils.isNumeric(StringUtils.substring(rechnungsnummer, 1)) ? "Rechnung: " + rechnungsnummer : rechnungsnummer;
        final String formattedRechnungsbetrag = new DecimalFormat("#0.00").format(rechnungsbetrag);
        return Joiner.on(";").join(formattedDate, verwendungszweck,
                StringUtils.startsWith(formattedRechnungsbetrag, "-") ? "" : formattedRechnungsbetrag,
                !StringUtils.startsWith(formattedRechnungsbetrag, "-") ? "" : formattedRechnungsbetrag, gesamtBetrag, "\r\n");
    }

}
