package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;
import de.sg.computerinsel.tools.kassenbuch.model.Kassenbestand;

/**
 * @author Sita Geßner
 */
public class KassenstandBerechnenGUI {

    private static final String STANDARD_VALUE_BERECHNEN = "0";
    
    private final Kassenbestand bestand = new Kassenbestand();

    private final JFrame main;

    private final Einstellungen einstellungen;

    public KassenstandBerechnenGUI(final JFrame main, final Einstellungen einstellungen) {
        this.main = main;
        this.einstellungen = einstellungen;
    }

    public JPanel createPanelKassenstandBerechnen() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(17, 3));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine500(), bestand.getErgebnisScheine500(),
                Kassenbestand.SCHEIN_500.toString(), Kassenbestand.SCHEIN_500);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine200(), bestand.getErgebnisScheine200(),
                Kassenbestand.SCHEIN_200.toString(), Kassenbestand.SCHEIN_200);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine100(), bestand.getErgebnisScheine100(),
                Kassenbestand.SCHEIN_100.toString(), Kassenbestand.SCHEIN_100);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine50(), bestand.getErgebnisScheine50(),
                Kassenbestand.SCHEIN_50.toString(), Kassenbestand.SCHEIN_50);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine20(), bestand.getErgebnisScheine20(),
                Kassenbestand.SCHEIN_20.toString(), Kassenbestand.SCHEIN_20);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine10(), bestand.getErgebnisScheine10(),
                Kassenbestand.SCHEIN_10.toString(), Kassenbestand.SCHEIN_10);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine5(), bestand.getErgebnisScheine5(),
                Kassenbestand.SCHEIN_5.toString(), Kassenbestand.SCHEIN_5);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen2(), bestand.getErgebnisMuenzen2(),
                Kassenbestand.MUENZE_2.toString(), Kassenbestand.MUENZE_2);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen1(), bestand.getErgebnisMuenzen1(),
                Kassenbestand.MUENZE_1.toString(), Kassenbestand.MUENZE_1);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen50(), bestand.getErgebnisMuenzen50(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_50), Kassenbestand.MUENZE_50);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen20(), bestand.getErgebnisMuenzen20(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_20), Kassenbestand.MUENZE_20);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen10(), bestand.getErgebnisMuenzen10(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_10), Kassenbestand.MUENZE_10);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen5(), bestand.getErgebnisMuenzen5(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_5), Kassenbestand.MUENZE_5);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen2Cent(), bestand.getErgebnisMuenzen2Cent(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_2_CENT), Kassenbestand.MUENZE_2_CENT);
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen1Cent(), bestand.getErgebnisMuenzen1Cent(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_1_CENT), Kassenbestand.MUENZE_1_CENT);
        panel.add(new JLabel("Differenz"));
        panel.add(new JLabel());
        panel.add(new JLabel("Gesamt Kassenbuch"));
        panel.add(new JLabel("Gesamt Kasse"));
        panel.add(bestand.getDifferenzBetrag());
        bestand.getDifferenzBetrag().setEditable(false);
        panel.add(new JLabel());
        panel.add(bestand.getGesamtBetragKassenbuch());
        panel.add(bestand.getGesamtErgebnis());
        return panel;
    }

    private void addRowToPanelKassenstandBerechnen(final JPanel panel, final JTextField anzahlFeld, final JTextField ergebnisFeld,
            final String betrag, final BigDecimal multiplier) {
        panel.add(anzahlFeld);
        panel.add(new JLabel("x " + betrag));
        panel.add(getMoneyPictureAsComponent(betrag.replace(",", "")));
        panel.add(ergebnisFeld);
        ergebnisFeld.setText(STANDARD_VALUE_BERECHNEN);
        ergebnisFeld.setEditable(false);
        ergebnisFeld.setFocusable(false);
        addActionListener(anzahlFeld, ergebnisFeld, multiplier);
    }
    
    private void addActionListener(final JTextField anzahlFeld, final JTextField ergebnisFeld, final BigDecimal multiplier) {
        anzahlFeld.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(final KeyEvent e) {
                anzahlFeld.setText(KassenstandBerechnenUtils.getNormalizedAnzahl(anzahlFeld.getText()));
                berechneErgebnis();
                berechneGesamtergebnis();
                setzeGesamtbetragKassenbuch();
                berechneDifferenz();
                colorDifferenzFeld();
            }

            private void setzeGesamtbetragKassenbuch() {
                try {
                    bestand.getGesamtBetragKassenbuch().setText(
                            KassenstandBerechnenUtils.getGesamtbetragKassenbuch(einstellungen.getDateipfadText(),
                                    einstellungen.getAblageverzeichnisText()));
                } catch (final IllegalStateException e) {
                    JOptionPane.showMessageDialog(main, e.getMessage());
                }
            }

            private void berechneErgebnis() {
                ergebnisFeld.setText(KassenstandBerechnenUtils.berechneErgebnis(anzahlFeld.getText(), multiplier));
            }

            private void berechneGesamtergebnis() {
                bestand.getGesamtErgebnis().setText(KassenstandBerechnenUtils.berechneGesamtergebnis(bestand));
            }

            private void berechneDifferenz() {
                bestand.getDifferenzBetrag().setText(
                        KassenstandBerechnenUtils.berechneDifferenz(bestand.getGesamtErgebnis().getText(), bestand
                                .getGesamtBetragKassenbuch().getText()));
            }

            private void colorDifferenzFeld() {
                final String differenz = bestand.getDifferenzBetrag().getText();
                if ("0,00".equals(differenz)) {
                    bestand.getDifferenzBetrag().setBackground(Color.GREEN);
                } else if (StringUtils.startsWith(differenz, "-")) {
                    bestand.getDifferenzBetrag().setBackground(Color.RED);
                } else {
                    bestand.getDifferenzBetrag().setBackground(Color.YELLOW);
                }
            }

            @Override
            public void keyTyped(final KeyEvent e) {
                // do nothing
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                }
                final boolean anzahlFeldIsBlank = StringUtils.isBlank(anzahlFeld.getText());
                if (anzahlFeldIsBlank || StringUtils.isNumeric(anzahlFeld.getText())) {
                    final int val = anzahlFeldIsBlank ? 0 : Integer.valueOf(anzahlFeld.getText());
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        anzahlFeld.setText(String.valueOf(val + 1));
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN && val >= 1) {
                        anzahlFeld.setText(String.valueOf(val - 1));
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT && val >= 10) {
                        anzahlFeld.setText(String.valueOf(val - 10));
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        anzahlFeld.setText(String.valueOf(val + 10));
                    }
                }
            }
        });
    }

    private Component getMoneyPictureAsComponent(final String filename) {
        return new JLabel(new ImageIcon(getClass().getResource("pictures/" + filename + ".png")));
    }
    
}
