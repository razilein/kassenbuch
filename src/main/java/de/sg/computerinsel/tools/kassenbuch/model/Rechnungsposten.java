package de.sg.computerinsel.tools.kassenbuch.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
@AllArgsConstructor
public class Rechnungsposten {

    private int menge;

    private String bezeichnung;

    private BigDecimal preis;

    private BigDecimal rabatt;

    private BigDecimal gesamt;

}
