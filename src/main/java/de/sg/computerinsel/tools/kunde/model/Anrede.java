package de.sg.computerinsel.tools.kunde.model;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Anrede {

    UNBEKANNT(null, "", ""), HERR(1, "Herr", "Sehr geehrter Herr "), FRAU(2, "Frau", "Sehr geehrte Frau "), FIRMA(3, "Firma",
            "Sehr geehrte Damen und Herren"), ALLGEMEIN(4, "Allgemein", "Sehr geehrte/r ");

    private final Integer code;

    private final String bezeichnung;

    private final String briefAnrede;

    public static Anrede getByCode(final Integer code) {
        return Arrays.stream(Anrede.values()).filter(a -> a.getCode() == code).findFirst().orElse(Anrede.UNBEKANNT);
    }

    public static Anrede getByBriefAnrede(final String anrede) {
        return Arrays.stream(Anrede.values()).filter(a -> a != Anrede.UNBEKANNT && StringUtils.startsWith(anrede, a.getBriefAnrede()))
                .findFirst().orElse(Anrede.UNBEKANNT);
    }

}
