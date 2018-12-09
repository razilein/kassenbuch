package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public final class KassenbuchBearbeitenUtils {

    private static final int INDEX_LINE_DATE = 0;

    static final int INDEX_LINE_ART = 1;

    static final int INDEX_LINE_SUM = 4;

    static final int MAX_LENGTH_LINE = 5;

    public static List<File> addKassenbuchEintrag(final String filePath, final Rechnung rechnung) {
        final List<Rechnung> rechnungen = readRechnungenFromCsvFile(filePath);
        rechnungen.add(rechnung);
        final Rechnung ausgangsRechnung = rechnungen.get(0);
        rechnungen.remove(0);
        final File csvFile = KassenbuchErstellenUtils.createCsv(rechnungen, ausgangsRechnung,
                filePath.substring(0, filePath.lastIndexOf(File.separator)));
        final File pdfFile = KassenbuchErstellenUtils.createPdf(rechnungen, ausgangsRechnung,
                filePath.substring(0, filePath.lastIndexOf(File.separator)));
        deleteOldFiles(filePath, FilenameUtils.removeExtension(filePath) + ".pdf");
        return Arrays.asList(csvFile, pdfFile);
    }

    private static void deleteOldFiles(final String csvFilePath, final String pdfFilePath) {
        FileUtils.deleteQuietly(new File(csvFilePath));
        FileUtils.deleteQuietly(new File(pdfFilePath));
    }

    public static Rechnung createNeueEintragung(final String verwendungstext, final Date datum, final BigDecimal betrag,
            final boolean isNegative) {
        final Rechnung rechnung = new Rechnung();
        rechnung.setRechnungsbetrag(getBetrag(betrag, isNegative));
        rechnung.setRechnungsdatum(datum);
        rechnung.setRechnungsnummer(verwendungstext);
        log.debug("Eintrag erstellt: {}", rechnung);
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
        try {
            int counter = 0;
            for (final String line : FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8)) {
                items = line.split(";");
                if (items.length == MAX_LENGTH_LINE) {
                    // Gesamtbeträge und Ausgangsbeträge ignorieren, außer ersten Ausgangsbetrag
                    if (isZeileGesamtbetragOrAusgangsbetrag(items, counter)) {
                        log.debug("Gesamtbetrag vom {}: {}", items[INDEX_LINE_DATE], items[INDEX_LINE_SUM]);
                        continue;
                    } else {
                        final Rechnung rechnung = createRechnung(items);
                        rechnungen.add(rechnung);
                        log.debug("Erfolgreich eingelesene Rechnung: {}", rechnung);
                    }
                } else if (items.length <= 1) {
                    log.debug("Leerzeile in CSV-Datei gefunden und überspringen.");
                } else {
                    logUngueltigeZeilen(items);
                }
                counter++;
            }
        } catch (final IOException e) {
            log.error("Fehler beim Lesen der Datei {}, {}", filePath, e.getMessage());
        } catch (final DateTimeParseException e) {
            log.error("Rechnungsdatum: '{}' kann nicht geparst werden: {} ", items == null ? null : items[INDEX_LINE_DATE], e.getMessage());
        } catch (final NumberFormatException e) {
            log.error("Fehler beim Lesen des Rechnungsbetrages: {}, {}", items == null ? null : extractBetrag(items), e.getMessage());
        }
        return rechnungen;
    }

    private static boolean isZeileGesamtbetragOrAusgangsbetrag(final String[] items, final int counter) {
        return items[INDEX_LINE_ART].contains(Rechnung.GESAMTBETRAG)
                || (items[INDEX_LINE_ART].contains(Rechnung.AUSGANGSBETRAG) && counter > 0);
    }

    private static void logUngueltigeZeilen(final String[] items) {
        final StringBuilder ungueltigeZeile = new StringBuilder();
        for (final String item : items) {
            ungueltigeZeile.append(item);
            ungueltigeZeile.append("; ");
        }
        log.warn("Ungültige Zeile beim Auslesen der CSV-Datei gefunden: {}. Zeile wurde ignoriert.", ungueltigeZeile);
    }

    private static Rechnung createRechnung(final String[] items) {
        final Rechnung rechnung = new Rechnung();
        rechnung.setRechnungsdatum(StringUtils.isNotBlank(items[INDEX_LINE_DATE]) ? DateUtils.parseDate(items[INDEX_LINE_DATE]) : null);
        rechnung.setRechnungsnummer(StringUtils.replace(items[INDEX_LINE_ART], "Rechnung: ", ""));
        rechnung.setRechnungsbetrag(new BigDecimal(normalizeCurrencyValue(extractBetrag(items))));
        return rechnung;
    }

    static String extractBetrag(final String[] items) {
        return items[2] == null || StringUtils.isBlank(items[2]) ? items[3] : items[2];
    }

    public static String normalizeCurrencyValue(final String value) {
        return StringUtils.replaceEach(value, new String[] { "€", "EUR", "," }, new String[] { "", "", "." }).trim();
    }
}
