package de.sg.computerinsel.tools.kassenbuch.rest.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sita Geßner
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kassenstand {

    private int anzahl;

    private BigDecimal betrag;

    private String key;

    private BigDecimal multi;

    public Kassenstand(final int anzahl, final String key) {
        this.anzahl = anzahl;
        this.key = key;
    }

}
