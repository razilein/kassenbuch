package de.sg.computerinsel.tools.kassenbuch.model;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Zahlart {

    BAR("BAR"), EC("EC"), UEBERWEISUNG("ÜBERWEISUNG");

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
