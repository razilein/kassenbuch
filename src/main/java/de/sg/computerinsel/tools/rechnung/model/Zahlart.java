package de.sg.computerinsel.tools.rechnung.model;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Zahlart {

    BAR(0, "BAR"), EC(1, "EC"), UEBERWEISUNG(2, "ÜBERWEISUNG"), PAYPAL(3, "PAYPAL");

    private final int code;

    private final String bezeichnung;

    public static Zahlart getByBezeichnung(final String bezeichnung) {
        return Arrays.asList(Zahlart.values()).stream().filter(z -> StringUtils.endsWithIgnoreCase(bezeichnung, z.getBezeichnung()))
                .findFirst().orElse(null);
    }

    public static Zahlart getByCode(final int code) {
        return Arrays.asList(Zahlart.values()).stream().filter(z -> code == z.getCode()).findFirst().orElse(null);
    }

}
