package de.sg.computerinsel.tools.kassenbuch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;

/**
 * @author Sita Geßner
 */
public final class KassenbuchBearbeitenUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(KassenbuchBearbeitenUtils.class);

	private KassenbuchBearbeitenUtils() {
	}
	
	public static File addKassenbuchEintrag(final String filePath, final String verwendungstext, final Date datum, final BigDecimal betrag,
	        final boolean isNegative) {
		final List<Rechnung> rechnungen = readRechnungenFromCsvFile(filePath);
		rechnungen.add(createNeueEintragung(verwendungstext, datum, betrag, isNegative));
		final Rechnung ausgangsRechnung = rechnungen.get(0);
		rechnungen.remove(0);
		final File csvFile = KassenbuchErstellenUtils.createCsv(rechnungen, ausgangsRechnung,
		        filePath.substring(0, filePath.lastIndexOf(File.separator)));
		KassenbuchErstellenUtils.createPdf(rechnungen, ausgangsRechnung, filePath.substring(0, filePath.lastIndexOf(File.separator)));
		return csvFile;
	}
	
	private static Rechnung createNeueEintragung(final String verwendungstext, final Date datum, final BigDecimal betrag,
			final boolean isNegative) {
		final Rechnung rechnung = new Rechnung();
		rechnung.setRechnungsbetrag(getBetrag(betrag, isNegative));
		rechnung.setRechnungsdatum(datum);
		rechnung.setRechnungsnummer(verwendungstext);
		LOGGER.info("Eintrag  hinzugefügt: {}", rechnung);
		return rechnung;
	}
	
	/**
	 * Wenn der Radiobutton <code>-</code> gesetzt wurde, muss der Betrag negativ sein werden.<br>
	 * Wenn der Radiobutton <code>+</code> gesetzt wurde, muss der Betrag positiv sein werden.
	 *
	 * @param betrag
	 * @param isNegative
	 * @return BigDecimal
	 */
	private static BigDecimal getBetrag(final BigDecimal betrag, final boolean isNegative) {
		BigDecimal result = betrag;
		final boolean isBetragPositiv = BigDecimal.ZERO.compareTo(betrag) < 0;
		final boolean isBetragNegativ = BigDecimal.ZERO.compareTo(betrag) > 0;
		if (isBetragPositiv && isNegative || isBetragNegativ && !isNegative) {
			result = result.multiply(new BigDecimal("-1"));
		}
		return result;
	}

	private static List<Rechnung> readRechnungenFromCsvFile(final String filePath) {
		final List<Rechnung> rechnungen = new ArrayList<>();
		String[] items = null;
		try (final BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = "";
			while ((line = br.readLine()) != null) {

				items = line.split(";");

				if (items.length == 3) {
					if (items[1].contains("Gesamtbetrag") && StringUtils.isBlank(items[0])) {
						LOGGER.info("Gesamtbetrag der Datei {}: {}", filePath, items[2]);
					} else {
						final Rechnung rechnung = new Rechnung();
						rechnung.setRechnungsdatum(StringUtils.isNotBlank(items[0]) ? KassenbuchErstellenUtils.DATE_FORMAT.parse(items[0])
						        : null);
						rechnung.setRechnungsnummer(StringUtils.replace(items[1], "Rechnung: ", ""));
						rechnung.setRechnungsbetrag(new BigDecimal(normalizeCurrencyValue(items[2])));
						rechnungen.add(rechnung);
						LOGGER.info("Erfolgreich eingelesene Rechnung: {}", rechnung);
					}
				} else {
					LOGGER.info("Ungültige Zeile beim Auslesen der CSV-Datei gefunden: {}. Zeile wurde ignoriert.", items.toString());
				}
			}
		} catch (final IOException e) {
			LOGGER.error("Fehler beim Lesen der Datei {}, {}", filePath, e.getMessage());
		} catch (final ParseException e) {
			LOGGER.error("Rechnungsdatum: '{}' kann nicht geparst werden: {} ", items == null ? null : items[0], e.getMessage());
		} catch (final NumberFormatException e) {
			LOGGER.error("Fehler beim Lesen des Rechnungsbetrages: {}", items == null ? null : items[2], e.getMessage());
		}
		return rechnungen;
	}
	
	public static String normalizeCurrencyValue(final String value) {
		return StringUtils.replaceEach(value, new String[] { "€", "EUR", "," }, new String[] { "", "", "." }).trim();
	}
}
