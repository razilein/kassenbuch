package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
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

import de.sg.computerinsel.tools.kassenbuch.model.Kassenbestand;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;

/**
 * @author Sita Geßner
 */
public class KassenbuchGUI {
	
	private final Logger LOGGER = LoggerFactory.getLogger(KassenbuchGUI.class);
	
	private static final String STANDARD_VALUE_BERECHNEN = "0";
	
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
	
	private final Kassenbestand bestand = new Kassenbestand();
	
	/*
	 * Kassenstand berechnen
	 */

	public void create(final JFrame main) {
		this.main = main;
		main.setIconImage(new ImageIcon(getClass().getResource("pictures/kasse.png")).getImage());
		main.setTitle("Kassenbuchprogramm V1.0.1-SNAPSHOT © Sita Geßner");

		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Kassenbuch erstellen", createPanelKassenbuchErstellen());
		tabbedPane.addTab("Kassenbuch editieren", createPanelKassenbuchEditieren());
		tabbedPane.addTab("Kassenstand berechnen", createPanelKassenstandBerechnen());
		tabbedPane.addTab("Einstellungen", createPanelEinstellungen());
		main.add(tabbedPane);
	}
	
	private JPanel createPanelEinstellungen() {
		final JPanel panel = new JPanel();
		final JButton btnRememberSettings = new JButton("Einstellungen speichern");
		btnRememberSettings.addActionListener(getActionListenerBtnEinstellungenMerken());
		final GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(new JLabel("Rechnungsverzeichnis"))
				.addComponent(rechnungsverzeichnis)
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(new JLabel("Ablageverzeichnis"))
						.addComponent(ablageverzeichnis))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(btnRememberSettings)));
		
		final Properties settings = SettingsUtils.loadSettings();
		if (settings != null) {
			rechnungsverzeichnis.setText(settings.getProperty(SettingsUtils.PROP_RECHNUNGSVERZEICHNIS));
			ablageverzeichnis.setText(settings.getProperty(SettingsUtils.PROP_ABLAGEVERZEICHNIS));
		}
		
		panel.add(btnRememberSettings);

		return panel;
	}
	
	private JPanel createPanelKassenstandBerechnen() {
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
							KassenstandBerechnenUtils.getGesamtbetragKassenbuch(dateiPfad.getText(), ablageverzeichnis.getText()));
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
		
		final Properties settings = SettingsUtils.loadSettings();
		if (settings != null) {
			ausgangsbetrag.setText(settings.getProperty(SettingsUtils.PROP_AUSGANGSBETRAG));
		}
		zeitraumBis.setText(KassenbuchErstellenUtils.DATE_FORMAT.format(new Date()));
		
		final GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(layout
		        .createSequentialGroup()
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
		        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnStart)));
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
