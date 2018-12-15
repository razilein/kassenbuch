package de.sg.computerinsel.tools.rechnung.model;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Zahlart {

    BAR(0, "BAR"), EC(1, "EC"), UEBERWEISUNG(2, "ÜBERWEISUNG"), PAYPAL(3, "PAYPAL");

    final int code;

    final String bezeichnung;

    public static Zahlart getByBezeichnung(final String bezeichnung) {
        Zahlart art = null;
        for (final Zahlart zahlart : Zahlart.values()) {
            if (StringUtils.endsWithIgnoreCase(bezeichnung, zahlart.getBezeichnung())) {
                art = zahlart;
                break;
            }
        }
        return art;
    }

}
