package de.sg.computerinsel.tools.kassenbuch;

import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;

/**
 * @author Sita Geßner
 */
public class EinstellungenGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EinstellungenGUI.class);

    private final JFrame main;

    private final Einstellungen einstellungen;

    public EinstellungenGUI(final JFrame main, final Einstellungen einstellungen) {
        this.main = main;
        this.einstellungen = einstellungen;
    }

    public JPanel createPanelEinstellungen() {
        final JPanel panel = new JPanel();
        final JButton btnRememberSettings = new JButton("Einstellungen speichern");
        btnRememberSettings.addActionListener(getActionListenerBtnEinstellungenMerken());
        final GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(new JLabel("Rechnungsverzeichnis"))
                .addComponent(einstellungen.getRechnungsverzeichnis())
                .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(new JLabel("Ablageverzeichnis"))
                                .addComponent(einstellungen.getAblageverzeichnis()))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(btnRememberSettings)));
        panel.add(btnRememberSettings);

        return panel;
    }

    private ActionListener getActionListenerBtnEinstellungenMerken() {
        return e -> {
            SettingsUtils.saveSettings(einstellungen.getRechnungsverzeichnisText(), einstellungen.getAblageverzeichnisText(),
                    einstellungen.getAusgangsbetragText(), einstellungen.getDateipfadText());
            JOptionPane.showMessageDialog(main, "Die Einstellungen wurden gespeichert.");
            LOGGER.info(
                    "Einstellungen gespeichert: Rechnungsverzeichnis: {}, Ablageverzeichnis: {}, Ausgangsbetrag: {}, Letzte CSV-Datei: {}",
                    einstellungen.getRechnungsverzeichnisText(), einstellungen.getAblageverzeichnisText(),
                    einstellungen.getAusgangsbetragText(), einstellungen.getDateipfadText());
        };
    }
}
