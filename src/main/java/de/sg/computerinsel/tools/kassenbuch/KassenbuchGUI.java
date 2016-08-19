package de.sg.computerinsel.tools.kassenbuch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;

/**
 * @author Sita Geßner
 */
public class KassenbuchGUI {
	
	private final Logger LOGGER = LoggerFactory.getLogger(KassenbuchGUI.class);
	
	private JFrame main;
	
	/*
	 * Kassenbuch erstellen
	 */
	
	private final JTextField rechnungsverzeichnis = new JTextField(50);
	
	private final JTextField ablageverzeichnis = new JTextField(50);

	private final JTextField zeitraumVon = new JTextField(10);

	private final JTextField zeitraumBis = new JTextField(10);

	private final JTextField ausgangsbetrag = new JTextField(50);

	/*
	 * Kassenbuch bearbeiten
	 */
	private final JTextField dateiPfad = new JTextField(50);

	private final JTextField eintragungsDatum = new JTextField(10);

	private final JTextField eintragungsBetrag = new JTextField(10);

	private final JTextField verwendungszweck = new JTextField(50);
	
	private final JRadioButton eintragungsArtEingang = new JRadioButton("+");
	
	private final JRadioButton eintragungsArtAusgang = new JRadioButton("-", true);
	
	public void create(final JFrame main) {
		this.main = main;
		
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Kassenbuch erstellen", createPanelKassenbuchErstellen());
		tabbedPane.addTab("Kassenbuch editieren", createPanelKassenbuchEditieren());
		main.add(tabbedPane);
	}
	
	private JPanel createPanelKassenbuchEditieren() {
		final JPanel panel = new JPanel();
		final JButton btnStart = new JButton("Eintrag hinzufügen");
		final Properties settings = SettingsUtils.loadSettings();
		if (settings != null) {
			dateiPfad.setText(settings.getProperty(SettingsUtils.PROP_LETZTE_CSV));
		}
		eintragungsDatum.setText(KassenbuchErstellenUtils.DATE_FORMAT.format(new Date()));
		btnStart.addActionListener(getActionListenerBtnKassenbuchBearbeiten());

		final GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		
		final ButtonGroup eintragungsArt = new ButtonGroup();
		eintragungsArt.add(eintragungsArtAusgang);
		eintragungsArt.add(eintragungsArtEingang);
		
		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addComponent(new JLabel("Zu bearbeitende CSV-Datei (mit Dateipfad)"))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(dateiPfad))
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
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnStart)));
		
		return panel;
	}
	
	private ActionListener getActionListenerBtnKassenbuchBearbeiten() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				boolean validParameters = true;
				final String filePath = dateiPfad.getText();
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
					final File csvFile = KassenbuchBearbeitenUtils.addKassenbuchEintrag(filePath, verwendungszweck.getText(), date, betrag,
					        eintragungsArtAusgang.isSelected() && !eintragungsArtEingang.isSelected());
					dateiPfad.setText(csvFile.getAbsolutePath());
					SettingsUtils.setPropertyLastCsvFile(csvFile.getAbsolutePath());
					LOGGER.info("Kassenbuch-Bearbeitung beendet.");
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

	private JPanel createPanelKassenbuchErstellen() {
		final JPanel panel = new JPanel();
		final JButton btnStart = new JButton("Kassenbuch erstellen");
		btnStart.addActionListener(getActionListenerBtnKassenbuchErstellen());
		
		final JButton btnRememberSettings = new JButton("Einstellungen speichern");
		btnRememberSettings.addActionListener(getActionListenerBtnEinstellungenMerken());
		
		final Properties settings = SettingsUtils.loadSettings();
		if (settings != null) {
			rechnungsverzeichnis.setText(settings.getProperty(SettingsUtils.PROP_RECHNUNGSVERZEICHNIS));
			ablageverzeichnis.setText(settings.getProperty(SettingsUtils.PROP_ABLAGEVERZEICHNIS));
			ausgangsbetrag.setText(settings.getProperty(SettingsUtils.PROP_AUSGANGSBETRAG));
		}
		zeitraumBis.setText(KassenbuchErstellenUtils.DATE_FORMAT.format(new Date()));
		
		final GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(layout
		        .createSequentialGroup()
		        .addComponent(new JLabel("Rechnungsverzeichnis"))
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(rechnungsverzeichnis))
		        .addComponent(new JLabel("Ablageverzeichnis"))
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(ablageverzeichnis))
		        .addGroup(
		                layout.createSequentialGroup()
		                        .addGroup(
		                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                                        .addComponent(new JLabel("Rechnungsdatum von")).addComponent(zeitraumVon))
		                        .addGroup(
		                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                                        .addComponent(new JLabel("Rechnungsdatum bis")).addComponent(zeitraumBis)))
		        .addGroup(layout.createParallelGroup().addComponent(new JLabel("Ausgangsbetrag")))
										.addGroup(layout.createParallelGroup().addGroup(layout.createParallelGroup().addComponent(ausgangsbetrag)))
		        .addGroup(
		                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnRememberSettings).addComponent(btnStart)));
		return panel;
	}

	private ActionListener getActionListenerBtnKassenbuchErstellen() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				boolean validParameters = true;
				
				final String rechnungsPath = rechnungsverzeichnis.getText();
				validParameters = validParameters && validatePath(rechnungsPath, "Rechnungsverzeichnis");
				final String ablagePath = ablageverzeichnis.getText();
				validParameters = validParameters && validatePath(ablagePath, "Ablageverzeichnis");
				
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
				
				if (validParameters) {
					LOGGER.info("Kassenbuch-Erstellung wird gestartet.");
					startRechnungsablage(rechnungsPath, ablagePath, dateFrom, dateTo, startBetrag);
				}
			}

			private void startRechnungsablage(final String rechnungsPath, final String ablagePath, final Date dateFrom, final Date dateTo,
					final BigDecimal startBetrag) {
				File csvFile = null;
				File pdfFile = null;
				final List<Rechnung> files = KassenbuchErstellenUtils.readHtmlFiles(new File(rechnungsPath), dateFrom, dateTo);
				if (files.isEmpty()) {
					LOGGER.info("Keine Rechnungen gefunden.");
					JOptionPane.showMessageDialog(main,
							"Im angegebenen Verzeichnis '" + rechnungsPath + "' konnten keine BAR-Rechnungen im angegebenen Zeitraum vom "
									+ KassenbuchErstellenUtils.DATE_FORMAT.format(dateFrom) + " bis "
									+ KassenbuchErstellenUtils.DATE_FORMAT.format(dateTo) + " gefunden werden.");
				} else {
					final Rechnung ausgangsRechnung = createStartBetrag(startBetrag);
					csvFile = KassenbuchErstellenUtils.createCsv(files, ausgangsRechnung, ablagePath);
					pdfFile = KassenbuchErstellenUtils.createPdf(files, ausgangsRechnung, ablagePath);
					dateiPfad.setText(csvFile.getAbsolutePath());
					SettingsUtils.setPropertyLastCsvFile(csvFile.getAbsolutePath());
					LOGGER.info("Kassenbuch-Erstellung beendet.");
				}
				if (csvFile == null) {
					JOptionPane.showMessageDialog(main, "Fehler beim Erstellen der CSV-Datei. Siehe " + getJarExecutionDirectory() + "logs"
					        + System.getProperty("file.separator") + "system.logs für weitere Hinweise.");
				} else {
					JOptionPane.showMessageDialog(main,
							"Das Kassenbuch wurde erfolgreich erstellt und unter: \r\n'" + csvFile.getAbsolutePath() + "' ablegt.");
				}
				
				if (pdfFile == null) {
					JOptionPane.showMessageDialog(main, "Fehler beim Erstellen der PDF-Datei. Siehe " + getJarExecutionDirectory() + "logs"
					        + System.getProperty("file.separator") + "system.logs für weitere Hinweise.");
				} else {
					JOptionPane.showMessageDialog(main,
							"Das Kassenbuch wurde erfolgreich erstellt und unter: \r\n'" + pdfFile.getAbsolutePath() + "' ablegt.");
				}
			}

			private Rechnung createStartBetrag(final BigDecimal startBetrag) {
				final Rechnung rechnung = new Rechnung();
				rechnung.setRechnungsbetrag(startBetrag);
				rechnung.setRechnungsnummer("Ausgangsbetrag");
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
	
	private ActionListener getActionListenerBtnEinstellungenMerken() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				SettingsUtils.saveSettings(rechnungsverzeichnis.getText(), ablageverzeichnis.getText(), ausgangsbetrag.getText(),
						dateiPfad.getText());
				JOptionPane.showMessageDialog(main, "Die Einstellungen wurden gespeichert.");
				LOGGER.info(
						"Einstellungen gespeichert: Rechnungsverzeichnis: {}, Ablageverzeichnis: {}, Ausgangsbetrag: {}, Letzte CSV-Datei: {}",
						rechnungsverzeichnis.getText(), ablageverzeichnis.getText(), ausgangsbetrag.getText(), dateiPfad.getText());
			}
		};
	}

	/**
	 * Returns the path where the currently running JAR-file is located. Example value: C:\MyProject\build\jar\
	 *
	 * @return Path of the JAR-file
	 */
	public static String getJarExecutionDirectory() {
		String jarFile = null;
		String jarDirectory = null;
		int cutFileSeperator = 0;
		int cutSemicolon = -1;

		jarFile = System.getProperty("java.class.path");
		// Cut seperators
		cutFileSeperator = jarFile.lastIndexOf(System.getProperty("file.separator"));
		jarDirectory = jarFile.substring(0, cutFileSeperator);
		// Cut semicolons
		cutSemicolon = jarDirectory.lastIndexOf(';');
		jarDirectory = jarDirectory.substring(cutSemicolon + 1, jarDirectory.length());

		return jarDirectory + System.getProperty("file.separator");
	}
}
