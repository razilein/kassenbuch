package de.sg.computerinsel.tools.kassenbuch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.model.Kassenbestand;

/**
 * @author Sita Geßner
 */
public final class KassenstandBerechnenUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(KassenstandBerechnenUtils.class);

    private static final NumberFormat NF = NumberFormat.getInstance(Locale.GERMAN);

    private KassenstandBerechnenUtils() {
    }

    public static String berechneErgebnis(final String anzahl, final BigDecimal multiplier) {
        return NumberUtils.isNumber(anzahl) ? getFormattedBetrag(multiplier.multiply(new BigDecimal(anzahl)))
                : Kassenbestand.STANDARD_VALUE_BERECHNEN;
    }

    public static String getNormalizedAnzahl(final String anzahl) {
        final String normalizedAnzahl = StringUtils.isNotBlank(anzahl) ? StringUtils.stripStart(anzahl.replaceAll("[\\D]", ""),
                Kassenbestand.STANDARD_VALUE_BERECHNEN) : anzahl;
        return StringUtils.isBlank(normalizedAnzahl) ? Kassenbestand.STANDARD_VALUE_BERECHNEN : normalizedAnzahl;
    }

    public static String berechneGesamtergebnis(final Kassenbestand bestand) {
        final BigDecimal result = getBetrag(bestand.getErgebnisScheine500().getText())
                .add(getBetrag(bestand.getErgebnisScheine200().getText())).add(getBetrag(bestand.getErgebnisScheine100().getText()))
                .add(getBetrag(bestand.getErgebnisScheine50().getText())).add(getBetrag(bestand.getErgebnisScheine20().getText()))
                .add(getBetrag(bestand.getErgebnisScheine10().getText())).add(getBetrag(bestand.getErgebnisScheine5().getText()))
                .add(getBetrag(bestand.getErgebnisMuenzen2().getText())).add(getBetrag(bestand.getErgebnisMuenzen1().getText()))
                .add(getBetrag(bestand.getErgebnisMuenzen50().getText())).add(getBetrag(bestand.getErgebnisMuenzen20().getText()))
                .add(getBetrag(bestand.getErgebnisMuenzen10().getText())).add(getBetrag(bestand.getErgebnisMuenzen5().getText()))
                .add(getBetrag(bestand.getErgebnisMuenzen2Cent().getText())).add(getBetrag(bestand.getErgebnisMuenzen1Cent().getText()));
        return getFormattedBetrag(result);
    }

    public static String berechneDifferenz(final String kassenstand, final String kassenbuchStand) {
        return getFormattedBetrag(getBetrag(kassenstand).subtract(getBetrag(kassenbuchStand)));
    }

    public static String getGesamtbetragKassenbuch(final String dateipfad, final String ablageverzeichnis) {
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(dateipfad) && new File(dateipfad).exists()) {
            result = getGesamtbetragByKassenbuchCsv(dateipfad);
        } else if (StringUtils.isNotBlank(ablageverzeichnis) && new File(ablageverzeichnis).exists()) {
            result = getGesamtbetragByKassenbuchCsv(getNeustesKassenbuch(ablageverzeichnis));
        } else {
            throw new IllegalStateException(
                    "Bitte unter 'Kassenbuch erstellen' ein Ablageverzeichnis oder unter 'Kassenbuch editieren' eine zu bearbeitende CSV-Datei hinterlegen.");
        }
        return getFormattedBetrag(result);
    }

    private static BigDecimal getGesamtbetragByKassenbuchCsv(final String dateipfad) {
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(dateipfad) && new File(dateipfad).exists()) {
            String[] items = null;
            try (final BufferedReader br = new BufferedReader(new FileReader(dateipfad))) {
                String line = "";
                String betrag = StringUtils.EMPTY;
                while ((line = br.readLine()) != null) {
                    items = line.split(";");
                    if (items.length == KassenbuchBearbeitenUtils.MAX_LENGTH_LINE
                            && items[KassenbuchBearbeitenUtils.INDEX_LINE_ART].contains("Gesamtbetrag")) {
                        betrag = items[KassenbuchBearbeitenUtils.INDEX_LINE_SUM];
                    }
                }
                result = getBetrag(betrag);
            } catch (final IOException e) {
                LOGGER.error("Fehler beim Lesen der Datei {}, {}", dateipfad, e.getMessage());
            }
        }
        return result;
    }

    private static String getNeustesKassenbuch(final String ablageverzeichnis) {
        File neusteDatei = null;
        final File dir = new File(ablageverzeichnis);
        final File[] files = dir.listFiles((FileFilter) file -> file.isFile() && FilenameUtils.isExtension(file.getName(), "csv"));

        if (files.length > 0) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            neusteDatei = files[0];
        }

        return neusteDatei == null ? null : neusteDatei.getAbsolutePath();
    }

    private static BigDecimal getBetrag(final String ergebnis) {
        BigDecimal result = BigDecimal.ZERO;
        if (ergebnis != null) {
            try {
                result = new BigDecimal(NF.parse(ergebnis).toString());
            } catch (final ParseException e) {
                LOGGER.error("Fehler beim parsen des Betrags: {}. Der Betrag wird ignoriert.", ergebnis);
            }
        }
        return result;
    }

    public static String getFormattedBetrag(final BigDecimal betrag) {
        final DecimalFormat formatter = new DecimalFormat("##,##0.00");
        return formatter.format(betrag);
    }

}
