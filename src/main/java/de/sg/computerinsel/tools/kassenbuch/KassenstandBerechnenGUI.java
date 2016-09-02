package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> 17 - Anzahl in Kassenstand berechnen speichern
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;
import de.sg.computerinsel.tools.kassenbuch.model.Kassenbestand;

/**
 * @author Sita Geßner
 */
public class KassenstandBerechnenGUI extends BaseKassenbuchGUI {

    private final static Logger LOGGER = LoggerFactory.getLogger(KassenstandBerechnenGUI.class);

    private static final String STANDARD_VALUE_BERECHNEN = "0";

    private final Kassenbestand bestand = new Kassenbestand();

    private final JFrame main;

    private final Einstellungen einstellungen;

    public KassenstandBerechnenGUI(final JFrame main, final Einstellungen einstellungen) {
        this.main = main;
        this.einstellungen = einstellungen;
    }

    @Override
    public JPanel createPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(17, 3));

        final List<String> kassenstand = loadSettings();
        int counter = 0;
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine500(), bestand.getErgebnisScheine500(),
                Kassenbestand.SCHEIN_500.toString(), Kassenbestand.SCHEIN_500, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine200(), bestand.getErgebnisScheine200(),
                Kassenbestand.SCHEIN_200.toString(), Kassenbestand.SCHEIN_200, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine100(), bestand.getErgebnisScheine100(),
                Kassenbestand.SCHEIN_100.toString(), Kassenbestand.SCHEIN_100, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine50(), bestand.getErgebnisScheine50(),
                Kassenbestand.SCHEIN_50.toString(), Kassenbestand.SCHEIN_50, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine20(), bestand.getErgebnisScheine20(),
                Kassenbestand.SCHEIN_20.toString(), Kassenbestand.SCHEIN_20, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine10(), bestand.getErgebnisScheine10(),
                Kassenbestand.SCHEIN_10.toString(), Kassenbestand.SCHEIN_10, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlScheine5(), bestand.getErgebnisScheine5(),
                Kassenbestand.SCHEIN_5.toString(), Kassenbestand.SCHEIN_5, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen2(), bestand.getErgebnisMuenzen2(),
                Kassenbestand.MUENZE_2.toString(), Kassenbestand.MUENZE_2, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen1(), bestand.getErgebnisMuenzen1(),
                Kassenbestand.MUENZE_1.toString(), Kassenbestand.MUENZE_1, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen50(), bestand.getErgebnisMuenzen50(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_50), Kassenbestand.MUENZE_50, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen20(), bestand.getErgebnisMuenzen20(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_20), Kassenbestand.MUENZE_20, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen10(), bestand.getErgebnisMuenzen10(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_10), Kassenbestand.MUENZE_10, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen5(), bestand.getErgebnisMuenzen5(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_5), Kassenbestand.MUENZE_5, kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen2Cent(), bestand.getErgebnisMuenzen2Cent(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_2_CENT), Kassenbestand.MUENZE_2_CENT,
                kassenstand.get(counter++));
        addRowToPanelKassenstandBerechnen(panel, bestand.getAnzahlMuenzen1Cent(), bestand.getErgebnisMuenzen1Cent(),
                KassenstandBerechnenUtils.getFormattedBetrag(Kassenbestand.MUENZE_1_CENT), Kassenbestand.MUENZE_1_CENT,
                kassenstand.get(counter++));
        panel.add(new JLabel());
        panel.add(new JLabel("Differenz"));
        panel.add(new JLabel("Gesamt Kassenbuch"));
        panel.add(new JLabel("Gesamt Kasse"));
        final JButton btnSpeichern = new JButton("Speichern");
        btnSpeichern.addActionListener(getActionListener(panel));
        panel.add(btnSpeichern);
        panel.add(bestand.getDifferenzBetrag());
        bestand.getDifferenzBetrag().setEditable(false);
        panel.add(bestand.getGesamtBetragKassenbuch());
        panel.add(bestand.getGesamtErgebnis());
        berechne();
        return panel;
    }

    private List<String> loadSettings() {
        final List<String> kassenstand = new ArrayList<>();
        final String setting = (String) SettingsUtils.loadSettings().get(SettingsUtils.PROP_KASSENSTAND);
        if (setting != null) {
            final List<String> splittedSettings = Splitter.on("|").splitToList(setting);
            splittedSettings.forEach(s -> kassenstand.add(KassenstandBerechnenUtils.getNormalizedAnzahl(s)));
        } else {
            final String[] empty = new String[15];
            Arrays.fill(empty, Kassenbestand.STANDARD_VALUE_BERECHNEN);
            kassenstand.addAll(Arrays.asList(empty));
        }
        return kassenstand;
    }

    private ActionListener getActionListener(final JPanel panel) {
        return e -> {
            final BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
            panel.paint(img.getGraphics());
            final File outputfile = new File(einstellungen.getAblageverzeichnisText(),
                    KassenbuchErstellenUtils.DATE_FORMAT_FILES.format(new Date()) + "_kassenstand.png");
            try {
                ImageIO.write(img, "png", outputfile);
                JOptionPane.showMessageDialog(main, "Der Kassenstand wurde erfolgreich unter: \r\n'" + outputfile.getAbsolutePath()
                        + "' ablegt.");
                saveKassenstand();
            } catch (final IOException ex) {
                LOGGER.error("Screenshot vom Kassenstand konnte nicht gespeichert werden: {} {}", ex.getMessage(), ex);
            }

        };
    }

    private void saveKassenstand() {
        SettingsUtils.setPropertyKassenstand(bestand.toSettingsString());
    }

    private void addRowToPanelKassenstandBerechnen(final JPanel panel, final JTextField anzahlFeld, final JTextField ergebnisFeld,
            final String betrag, final BigDecimal multiplier, final String anzahl) {
        panel.add(anzahlFeld);
        anzahlFeld.setText(anzahl);
        panel.add(new JLabel("x " + betrag));
        panel.add(getMoneyPictureAsComponent(betrag.replace(",", "")));
        panel.add(ergebnisFeld);
        ergebnisFeld.setText(KassenstandBerechnenUtils.berechneErgebnis(anzahl, multiplier));
        ergebnisFeld.setEditable(false);
        ergebnisFeld.setFocusable(false);
        addActionListener(anzahlFeld, ergebnisFeld, multiplier);
    }

    private void berechne() {
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

    private void berechneGesamtergebnis() {
        bestand.getGesamtErgebnis().setText(KassenstandBerechnenUtils.berechneGesamtergebnis(bestand));
    }

    private void berechneDifferenz() {
        bestand.getDifferenzBetrag().setText(
                KassenstandBerechnenUtils.berechneDifferenz(bestand.getGesamtErgebnis().getText(), bestand.getGesamtBetragKassenbuch()
                        .getText()));
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

    private void addActionListener(final JTextField anzahlFeld, final JTextField ergebnisFeld, final BigDecimal multiplier) {
        anzahlFeld.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(final KeyEvent e) {
                anzahlFeld.setText(KassenstandBerechnenUtils.getNormalizedAnzahl(anzahlFeld.getText()));
                berechneErgebnis();
                berechne();
            }

            private void berechneErgebnis() {
                ergebnisFeld.setText(KassenstandBerechnenUtils.berechneErgebnis(anzahlFeld.getText(), multiplier));
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
