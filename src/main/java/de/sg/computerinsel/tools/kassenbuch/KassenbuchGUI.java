package de.sg.computerinsel.tools.kassenbuch;

import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;

/**
 * @author Sita Geßner
 */
public class KassenbuchGUI {

    private final Einstellungen einstellungen = new Einstellungen();

    public void create(final JFrame main) {
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/kasse.png")).getImage());
        main.setTitle("Kassenbuchprogramm V1.0.1 © Sita Geßner");
        loadSettings();
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Kassenbuch erstellen", new KassenbuchErstellenGUI(main, einstellungen).createPanelKassenbuchErstellen());
        tabbedPane.addTab("Kassenbuch bearbeiten", new KassenbuchBearbeitenGUI(main, einstellungen).createPanelKassenbuchBearbeiten());
        tabbedPane.addTab("Kassenstand berechnen", new KassenstandBerechnenGUI(main, einstellungen).createPanelKassenstandBerechnen());
        tabbedPane.addTab("Einstellungen", new EinstellungenGUI(main, einstellungen).createPanelEinstellungen());
        main.add(tabbedPane);
    }

    private void loadSettings() {
        final Properties settings = SettingsUtils.loadSettings();
        if (settings != null) {
            einstellungen.setRechnungsverzeichnisText(settings.getProperty(SettingsUtils.PROP_RECHNUNGSVERZEICHNIS));
            einstellungen.setAblageverzeichnisText(settings.getProperty(SettingsUtils.PROP_ABLAGEVERZEICHNIS));
            einstellungen.setDateipfadText(settings.getProperty(SettingsUtils.PROP_LETZTE_CSV));
            einstellungen.setAusgangsbetragText(settings.getProperty(SettingsUtils.PROP_AUSGANGSBETRAG));
        }
    }

}
