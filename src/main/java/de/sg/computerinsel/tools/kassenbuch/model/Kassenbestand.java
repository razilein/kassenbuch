package de.sg.computerinsel.tools.kassenbuch.model;

import java.math.BigDecimal;

import javax.swing.JTextField;

/**
 * @author Sita Geßner
 */
public class Kassenbestand {

    public static final BigDecimal SCHEIN_500 = new BigDecimal("500");

    public static final BigDecimal SCHEIN_200 = new BigDecimal("200");

    public static final BigDecimal SCHEIN_100 = new BigDecimal("100");

    public static final BigDecimal SCHEIN_50 = new BigDecimal("50");

    public static final BigDecimal SCHEIN_20 = new BigDecimal("20");

    public static final BigDecimal SCHEIN_10 = new BigDecimal("10");

    public static final BigDecimal SCHEIN_5 = new BigDecimal("5");

    public static final BigDecimal MUENZE_2 = new BigDecimal("2");

    public static final BigDecimal MUENZE_1 = new BigDecimal("1");

    public static final BigDecimal MUENZE_50 = new BigDecimal("0.5");

    public static final BigDecimal MUENZE_20 = new BigDecimal("0.2");

    public static final BigDecimal MUENZE_10 = new BigDecimal("0.1");

    public static final BigDecimal MUENZE_5 = new BigDecimal("0.05");

    public static final BigDecimal MUENZE_2_CENT = new BigDecimal("0.02");

    public static final BigDecimal MUENZE_1_CENT = new BigDecimal("0.01");

    /*
     * Geldscheine
     */

    private final JTextField anzahlScheine500 = new JTextField(5);

    private final JTextField ergebnisScheine500 = new JTextField(5);

    private final JTextField anzahlScheine200 = new JTextField(5);

    private final JTextField ergebnisScheine200 = new JTextField(5);

    private final JTextField anzahlScheine100 = new JTextField(5);

    private final JTextField ergebnisScheine100 = new JTextField(5);

    private final JTextField anzahlScheine50 = new JTextField(5);

    private final JTextField ergebnisScheine50 = new JTextField(5);

    private final JTextField anzahlScheine20 = new JTextField(5);

    private final JTextField ergebnisScheine20 = new JTextField(5);

    private final JTextField anzahlScheine10 = new JTextField(5);

    private final JTextField ergebnisScheine10 = new JTextField(5);

    private final JTextField anzahlScheine5 = new JTextField(5);

    private final JTextField ergebnisScheine5 = new JTextField(5);

    /*
     * Münzgeld
     */

    private final JTextField anzahlMuenzen2 = new JTextField(5);

    private final JTextField ergebnisMuenzen2 = new JTextField(5);

    private final JTextField anzahlMuenzen1 = new JTextField(5);

    private final JTextField ergebnisMuenzen1 = new JTextField(5);

    private final JTextField anzahlMuenzen50 = new JTextField(5);

    private final JTextField ergebnisMuenzen50 = new JTextField(5);

    private final JTextField anzahlMuenzen20 = new JTextField(5);

    private final JTextField ergebnisMuenzen20 = new JTextField(5);

    private final JTextField anzahlMuenzen10 = new JTextField(5);

    private final JTextField ergebnisMuenzen10 = new JTextField(5);

    private final JTextField anzahlMuenzen5 = new JTextField(5);

    private final JTextField ergebnisMuenzen5 = new JTextField(5);

    private final JTextField anzahlMuenzen2Cent = new JTextField(5);

    private final JTextField ergebnisMuenzen2Cent = new JTextField(5);

    private final JTextField anzahlMuenzen1Cent = new JTextField(5);

    private final JTextField ergebnisMuenzen1Cent = new JTextField(5);

    private final JTextField gesamtErgebnis = new JTextField(5);

    private final JTextField gesamtBetragKassenbuch = new JTextField(5);

    private final JTextField differenzBetrag = new JTextField(5);

    public JTextField getAnzahlScheine500() {
        return anzahlScheine500;
    }

    public JTextField getErgebnisScheine500() {
        return ergebnisScheine500;
    }

    public JTextField getAnzahlScheine200() {
        return anzahlScheine200;
    }

    public JTextField getErgebnisScheine200() {
        return ergebnisScheine200;
    }

    public JTextField getAnzahlScheine100() {
        return anzahlScheine100;
    }

    public JTextField getErgebnisScheine100() {
        return ergebnisScheine100;
    }

    public JTextField getAnzahlScheine50() {
        return anzahlScheine50;
    }

    public JTextField getErgebnisScheine50() {
        return ergebnisScheine50;
    }

    public JTextField getAnzahlScheine20() {
        return anzahlScheine20;
    }

    public JTextField getErgebnisScheine20() {
        return ergebnisScheine20;
    }

    public JTextField getAnzahlScheine10() {
        return anzahlScheine10;
    }

    public JTextField getErgebnisScheine10() {
        return ergebnisScheine10;
    }

    public JTextField getAnzahlScheine5() {
        return anzahlScheine5;
    }

    public JTextField getErgebnisScheine5() {
        return ergebnisScheine5;
    }

    public JTextField getAnzahlMuenzen2() {
        return anzahlMuenzen2;
    }

    public JTextField getErgebnisMuenzen2() {
        return ergebnisMuenzen2;
    }

    public JTextField getAnzahlMuenzen1() {
        return anzahlMuenzen1;
    }

    public JTextField getErgebnisMuenzen1() {
        return ergebnisMuenzen1;
    }

    public JTextField getAnzahlMuenzen50() {
        return anzahlMuenzen50;
    }

    public JTextField getErgebnisMuenzen50() {
        return ergebnisMuenzen50;
    }

    public JTextField getAnzahlMuenzen20() {
        return anzahlMuenzen20;
    }

    public JTextField getErgebnisMuenzen20() {
        return ergebnisMuenzen20;
    }

    public JTextField getAnzahlMuenzen10() {
        return anzahlMuenzen10;
    }

    public JTextField getErgebnisMuenzen10() {
        return ergebnisMuenzen10;
    }

    public JTextField getAnzahlMuenzen5() {
        return anzahlMuenzen5;
    }

    public JTextField getErgebnisMuenzen5() {
        return ergebnisMuenzen5;
    }

    public JTextField getAnzahlMuenzen2Cent() {
        return anzahlMuenzen2Cent;
    }

    public JTextField getErgebnisMuenzen2Cent() {
        return ergebnisMuenzen2Cent;
    }

    public JTextField getAnzahlMuenzen1Cent() {
        return anzahlMuenzen1Cent;
    }

    public JTextField getErgebnisMuenzen1Cent() {
        return ergebnisMuenzen1Cent;
    }

    public JTextField getGesamtErgebnis() {
        return gesamtErgebnis;
    }

    public JTextField getGesamtBetragKassenbuch() {
        return gesamtBetragKassenbuch;
    }

    public JTextField getDifferenzBetrag() {
        return differenzBetrag;
    }

}
