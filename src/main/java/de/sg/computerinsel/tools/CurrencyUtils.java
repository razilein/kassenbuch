package de.sg.computerinsel.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import lombok.experimental.UtilityClass;

/**
 * @author Sita Geßner
 */
@UtilityClass
public class CurrencyUtils {

    private static final DecimalFormat BETRAG_FORMAT = new DecimalFormat("#,###,##0.00");

    public static String format(final BigDecimal betrag) {
        return BETRAG_FORMAT.format(betrag);
    }

}
