package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;

public class KassenbuchErstellenGUI extends BaseKassenbuchGUI {

    private final Logger LOGGER = LoggerFactory.getLogger(KassenbuchErstellenGUI.class);

    private final JFrame main;

    private final JTextField zeitraumVon = new JTextField(10);

    private final JTextField zeitraumBis = new JTextField(10);

    private final JTextField ausgangsbetrag = new JTextField(15);

    private final JTextField ausgangsbetragDatum = new JTextField(8);

    private final Einstellungen einstellungen;

    public KassenbuchErstellenGUI(final JFrame main, final Einstellungen einstellungen) {
        this.main = main;
        this.einstellungen = einstellungen;
    }

    @Override
    public JPanel createPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton btnStart = new JButton("Kassenbuch erstellen");
        btnStart.addActionListener(getActionListenerBtnKassenbuchErstellen());

        final JButton btnAnzeigen = new JButton("Anzeigen");
        btnAnzeigen.addActionListener(getActionListenerBtnAnzeigen(einstellungen));

        ausgangsbetrag.setText(KassenbuchUtils.getAusgangsbetragFromLatestKassenbuch(einstellungen.getRechnungsverzeichnisText(),
                einstellungen.getAblageverzeichnisText()));
        final String currentDate = KassenbuchErstellenUtils.DATE_FORMAT.format(new Date());
        ausgangsbetragDatum.setText(currentDate);
        zeitraumVon.setText(currentDate);
        zeitraumBis.setText(currentDate);

        final JPanel datumPanel = new JPanel(new GridLayout(2, 2, 5, 0));
        datumPanel.setPreferredSize(new Dimension(350, 40));
        datumPanel.add(new JLabel("Rechnungsdatum von"));
        datumPanel.add(new JLabel("Rechnungsdatum bis"));
        datumPanel.add(zeitraumVon);
        datumPanel.add(zeitraumBis);
        panel.add(datumPanel);

        panel.add(new JLabel("Kassenstand Vortag als Ausgangsbetrag für den"));
        final JPanel ausgangsbetragPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ausgangsbetragPanel.setPreferredSize(new Dimension(600, 25));
        ausgangsbetrag.setHorizontalAlignment(SwingConstants.RIGHT);
        ausgangsbetragPanel.add(ausgangsbetrag);
        ausgangsbetragPanel.add(ausgangsbetragDatum);
        panel.add(ausgangsbetragPanel);

        final JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 10));
        btnPanel.setPreferredSize(new Dimension(450, 25));
        btnPanel.add(btnAnzeigen);
        btnPanel.add(btnStart);
        panel.add(btnPanel);

        return panel;
    }

    private ActionListener getActionListenerBtnKassenbuchErstellen() {
        return new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                boolean validParameters = true;

                validParameters = validParameters && validatePath(einstellungen.getRechnungsverzeichnisText(), "Rechnungsverzeichnis");
                validParameters = validParameters && validatePath(einstellungen.getAblageverzeichnisText(), "Ablageverzeichnis");

                Date dateFrom = null;
                try {
                    dateFrom = KassenbuchErstellenUtils.DATE_FORMAT.parse(zeitraumVon.getText());
                } catch (final ParseException e1) {
                    JOptionPane.showMessageDialog(main,
                            "Das angegebene Datum im Feld 'Rechnungsdatum von' besitzt kein gültiges Datumsformat. (dd.MM.yyyy)");
                    validParameters = false;
                }

                Date dateTo = null;
                try {
                    dateTo = KassenbuchErstellenUtils.DATE_FORMAT.parse(zeitraumBis.getText());
                } catch (final ParseException e1) {
                    JOptionPane.showMessageDialog(main,
                            "Das angegebene Datum im Feld 'Rechnungsdatum bis' besitzt kein gültiges Datumsformat. (dd.MM.yyyy)");
                    validParameters = false;
                }

                BigDecimal startBetrag = BigDecimal.ZERO;
                try {
                    startBetrag = new BigDecimal(KassenbuchBearbeitenUtils.normalizeCurrencyValue(ausgangsbetrag.getText()));
                } catch (final NumberFormatException e2) {
                    JOptionPane.showMessageDialog(main,
                            "Bitte geben Sie im Feld 'Ausgangsbetrag' einen gültigen Wert ein (ohne Währungssymbol).");
                    validParameters = false;
                }

                Date startBetragdatum = null;
                try {
                    startBetragdatum = KassenbuchErstellenUtils.DATE_FORMAT.parse(ausgangsbetragDatum.getText());
                } catch (final ParseException e1) {
                    JOptionPane.showMessageDialog(main,
                            "Das angegebene Datum im Feld 'Ausgangsdatum vom' besitzt kein gültiges Datumsformat. (dd.MM.yyyy)");
                    validParameters = false;
                }

                if (validParameters) {
                    LOGGER.info("Kassenbuch-Erstellung wird gestartet.");
                    startRechnungsablage(einstellungen.getRechnungsverzeichnisText(), einstellungen.getAblageverzeichnisText(), dateFrom,
                            dateTo, startBetrag, startBetragdatum);
                }
            }

            private void startRechnungsablage(final String rechnungsPath, final String ablagePath, final Date dateFrom, final Date dateTo,
                    final BigDecimal startBetrag, final Date startBetragdatum) {
                File csvFile = null;
                File pdfFile = null;
                final List<Rechnung> rechnungen = KassenbuchErstellenUtils.readHtmlFiles(new File(rechnungsPath), dateFrom, dateTo);
                if (rechnungen.isEmpty()) {
                    LOGGER.info("Keine Rechnungen gefunden.");
                    JOptionPane.showMessageDialog(main,
                            "Im angegebenen Verzeichnis '" + rechnungsPath + "' konnten keine BAR-Rechnungen im angegebenen Zeitraum vom "
                                    + KassenbuchErstellenUtils.DATE_FORMAT.format(dateFrom) + " bis "
                                    + KassenbuchErstellenUtils.DATE_FORMAT.format(dateTo) + " gefunden werden.");
                } else {
                    final Rechnung ausgangsRechnung = createStartBetrag(startBetrag, startBetragdatum);
                    csvFile = KassenbuchErstellenUtils.createCsv(rechnungen, ausgangsRechnung, ablagePath);
                    pdfFile = KassenbuchErstellenUtils.createPdf(rechnungen, ausgangsRechnung, ablagePath);
                    einstellungen.setDateipfadText(csvFile.getAbsolutePath());
                    SettingsUtils.setPropertyLastCsvFile(csvFile.getAbsolutePath());
                    ausgangsbetrag.setText(KassenbuchUtils.getAusgangsbetragFromLatestKassenbuch(
                            einstellungen.getRechnungsverzeichnisText(), einstellungen.getAblageverzeichnisText()));
                    LOGGER.info("Kassenbuch-Erstellung beendet.");

                }
                if (csvFile == null) {
                    JOptionPane.showMessageDialog(
                            main,
                            "Fehler beim Erstellen der CSV-Datei. Siehe " + KassenbuchUtils.getJarExecutionDirectory() + "logs"
                                    + System.getProperty("file.separator") + "system.logs für weitere Hinweise.");
                } else {
                    JOptionPane.showMessageDialog(main,
                            "Das Kassenbuch wurde erfolgreich erstellt und unter: \r\n'" + csvFile.getAbsolutePath() + "' ablegt.");
                }

                if (pdfFile == null) {
                    JOptionPane.showMessageDialog(
                            main,
                            "Fehler beim Erstellen der PDF-Datei. Siehe " + KassenbuchUtils.getJarExecutionDirectory() + "logs"
                                    + System.getProperty("file.separator") + "system.logs für weitere Hinweise.");
                } else {
                    JOptionPane.showMessageDialog(main,
                            "Das Kassenbuch wurde erfolgreich erstellt und unter: \r\n'" + pdfFile.getAbsolutePath() + "' ablegt.");
                }
            }

            private Rechnung createStartBetrag(final BigDecimal startBetrag, final Date startBetragdatum) {
                final Rechnung rechnung = new Rechnung();
                rechnung.setRechnungsdatum(startBetragdatum);
                rechnung.setRechnungsbetrag(startBetrag);
                rechnung.setRechnungsnummer(Rechnung.AUSGANGSBETRAG);
                return rechnung;
            }

            private boolean validatePath(final String path, final String messageDescription) {
                boolean result = true;
                if (StringUtils.isBlank(path) || !new File(path).exists() || !new File(path).isDirectory()) {
                    JOptionPane.showMessageDialog(main, "Bitte geben Sie ein gültiges " + messageDescription + " ein.");
                    LOGGER.info("Ungültiges {} '{}'", messageDescription, path);
                    result = false;
                } else if (StringUtils.isNoneBlank(path) && !new File(path).canWrite()) {
                    JOptionPane.showMessageDialog(main, "Auf das angegebene " + messageDescription + " besitzen Sie keine Schreibrechte.");
                    LOGGER.info("Keine Schreibrechte auf {} '{}'", messageDescription, path);
                    result = false;
                }
                return result;
            }

        };

    }

}