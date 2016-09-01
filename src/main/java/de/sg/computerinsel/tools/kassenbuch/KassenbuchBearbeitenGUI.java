package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;

/**
 * @author Sita Geßner
 */
public class KassenbuchBearbeitenGUI extends BaseKassenbuchGUI {

    private final Logger LOGGER = LoggerFactory.getLogger(KassenbuchBearbeitenGUI.class);

    private final JFrame main;

    private final JTextField eintragungsDatum = new JTextField(10);

    private final JTextField eintragungsBetrag = new JTextField(10);

    private final JTextField verwendungszweck = new JTextField(50);

    private final JRadioButton eintragungsArtEingang = new JRadioButton("+");

    private final JRadioButton eintragungsArtAusgang = new JRadioButton("-", true);

    private final DefaultTableModel tableModel = new DefaultTableModel() {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    private final Einstellungen einstellungen;

    public KassenbuchBearbeitenGUI(final JFrame main, final Einstellungen einstellungen) {
        this.main = main;
        this.einstellungen = einstellungen;
    }

    @Override
    public JPanel createPanel() {
        final JPanel panel = new JPanel();
        final JButton btnStart = new JButton("Eintrag hinzufügen");
        eintragungsDatum.setText(KassenbuchErstellenUtils.DATE_FORMAT.format(new Date()));
        btnStart.addActionListener(getActionListenerBtnKassenbuchBearbeiten());

        final JButton btnAnzeigen = new JButton("Anzeigen");
        btnAnzeigen.addActionListener(getActionListenerBtnAnzeigen(einstellungen));

        final GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);

        final ButtonGroup eintragungsArt = new ButtonGroup();
        eintragungsArt.add(eintragungsArtAusgang);
        eintragungsArt.add(eintragungsArtEingang);
        prepareTableModel();

        layout.setHorizontalGroup(layout
                .createSequentialGroup()
                .addComponent(new JLabel("Zu bearbeitende CSV-Datei (mit Dateipfad)"))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(einstellungen.getDateipfad()))
                .addComponent(new JLabel("Verwendungszweck"))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(verwendungszweck))
                .addGroup(
                        layout.createSequentialGroup()
                                .addGroup(
                                        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(new JLabel("Eintragungsdatum")).addComponent(eintragungsDatum))
                                .addGroup(
                                        layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(new JLabel("Betrag"))
                                                .addComponent(eintragungsArtAusgang).addComponent(eintragungsArtEingang)
                                                .addComponent(eintragungsBetrag)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnStart))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(createTablePane()))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnAnzeigen)));

        return panel;
    }

    private ActionListener getActionListenerBtnKassenbuchBearbeiten() {
        return new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                boolean validParameters = true;
                final String filePath = einstellungen.getDateipfadText();
                validParameters = validParameters && validateDateiPfad(filePath);
                validParameters = validParameters && validVerwendungszweck();

                Date date = null;
                try {
                    date = KassenbuchErstellenUtils.DATE_FORMAT.parse(eintragungsDatum.getText());
                } catch (final ParseException e1) {
                    JOptionPane.showMessageDialog(main,
                            "Das angegebene Datum im Feld 'Eintragungsdatum' besitzt kein gültiges Datumsformat. (dd.MM.yyyy)");
                    validParameters = false;
                }

                BigDecimal betrag = BigDecimal.ZERO;
                try {
                    betrag = new BigDecimal(KassenbuchBearbeitenUtils.normalizeCurrencyValue(eintragungsBetrag.getText()));
                } catch (final NumberFormatException e2) {
                    JOptionPane.showMessageDialog(main, "Bitte geben Sie im Feld 'Betrag' einen gültigen Wert ein (ohne Währungssymbol).");
                    validParameters = false;
                }

                if (validParameters) {
                    LOGGER.info("Kassenbuch-Bearbeitung gestartet.");
                    final Rechnung neuerEintrag = KassenbuchBearbeitenUtils.createNeueEintragung(verwendungszweck.getText(), date, betrag,
                            eintragungsArtAusgang.isSelected() && !eintragungsArtEingang.isSelected());
                    final File csvFile = KassenbuchBearbeitenUtils.addKassenbuchEintrag(filePath, neuerEintrag);
                    einstellungen.setDateipfadText(csvFile.getAbsolutePath());
                    SettingsUtils.setPropertyLastCsvFile(csvFile.getAbsolutePath());
                    einstellungen.setAusgangsbetragText(KassenbuchUtils.getAusgangsbetragFromLatestKassenbuch(
                            einstellungen.getRechnungsverzeichnisText(), einstellungen.getAblageverzeichnisText()));
                    LOGGER.info("Kassenbuch-Bearbeitung beendet.");
                    addTableEntry(neuerEintrag);
                    JOptionPane.showMessageDialog(main, "Der Eintrag wurde erfolgreich der Datei \n\r" + filePath + " hinzugefügt.");
                }
            }

            private boolean validVerwendungszweck() {
                final boolean validParameters = StringUtils.isNotBlank(verwendungszweck.getText().trim());
                if (!validParameters) {
                    JOptionPane.showMessageDialog(main, "Bitte geben Sie einen Verwendungszweck an.");
                }
                return validParameters;
            }

            private boolean validateDateiPfad(final String dateiPfad) {
                boolean result = true;
                final File file = new File(dateiPfad);
                if (StringUtils.isBlank(dateiPfad) || !file.exists()) {
                    JOptionPane.showMessageDialog(main, "Bitte geben Sie einen gültigen Dateipfad zu der zu bearbeitenden CSV-Datei an.");
                    LOGGER.info("Ungültiger Datepfad '{}'", dateiPfad);
                    result = false;
                } else if (StringUtils.isNoneBlank(dateiPfad) && !file.canWrite()) {
                    if (!FilenameUtils.isExtension(file.getName(), new String[] { "csv", "CSV" })) {
                        JOptionPane.showMessageDialog(main, "Die Datei " + file.getAbsolutePath() + " ist keine gültige CSV-Datei.");
                        LOGGER.info("Keine CSV-Datei  '{}'", file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(main, "Sie besitzen keine Schreibrechte auf die Datei." + file.getAbsolutePath());
                        LOGGER.info("Keine Schreibrechte auf Datei  '{}'", file.getAbsolutePath());
                    }
                    result = false;
                }
                return result;
            }
        };
    }

    private JScrollPane createTablePane() {
        final JScrollPane pane = new JScrollPane();
        pane.setPreferredSize(new Dimension(550, 200));
        pane.getViewport().add(table);
        return pane;
    }

    private void prepareTableModel() {
        tableModel.addColumn("Rechnungsdatum");
        tableModel.addColumn("Verwendungszweck");
        tableModel.addColumn("Eingang");
        tableModel.addColumn("Ausgang");
    }

    private void addTableEntry(final Rechnung rechnung) {
        final String rechnungsbetrag = KassenbuchErstellenUtils.BETRAG_FORMAT.format(rechnung.getRechnungsbetrag());
        tableModel.addRow(new String[] { KassenbuchErstellenUtils.DATE_FORMAT.format(rechnung.getRechnungsdatum()),
                rechnung.getRechnungsnummer(),
                KassenbuchErstellenUtils.isEingangssbetrag(rechnungsbetrag) ? rechnungsbetrag : StringUtils.EMPTY,
                KassenbuchErstellenUtils.isAusgangsbetrag(rechnungsbetrag) ? rechnungsbetrag : StringUtils.EMPTY });
    }

}
