package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnungsposten;
import de.sg.computerinsel.tools.kassenbuch.model.Zahlart;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public final class RechnungenEinlesenUtils {

    public static final DecimalFormat BETRAG_FORMAT = new DecimalFormat("#,###,##0.00");

    private static final String FORMAT_DATUM = "dd.MM.yyyy";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT_DATUM);

    public static final SimpleDateFormat DATE_FORMAT_FILES = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public static List<Rechnung> readHtmlFiles(final File directory, final Date dateFrom, final Date dateTo) {
        return readHtmlFiles(directory, dateFrom, dateTo, false);
    }

    public static List<Rechnung> readHtmlFilesWithPosten(final File directory, final Date dateFrom, final Date dateTo) {
        return readHtmlFiles(directory, dateFrom, dateTo, true);
    }

    private static List<Rechnung> readHtmlFiles(final File directory, final Date dateFrom, final Date dateTo, final boolean complete) {
        final File[] files = directory.listFiles((FilenameFilter) (dir, name) -> name.contains(".htm"));
        log.info("{} Rechnungen gefunden", files.length);

        int counterBarRechnungen = 0;
        final List<Rechnung> rechnungen = new ArrayList<>();
        for (final File file : files) {
            Rechnung rechnung = null;
            if (complete) {
                rechnung = parseCompleteFile(file, dateFrom, dateTo);
            } else if (isBarRechnung(file)) {
                rechnung = parseFile(file, dateFrom, dateTo);
            }
            if (rechnung != null) {
                rechnungen.add(rechnung);
                counterBarRechnungen++;
            }
        }
        log.info("{} Rechnungen verarbeitet", counterBarRechnungen);
        return rechnungen;

    }

    private static boolean isBarRechnung(final File file) {
        Boolean isBarRechnung = false;
        final String dateiname = file.getName();
        try {
            final Document doc = Jsoup.parse(file, Charsets.UTF_8.name());
            final Elements elements = doc.select("p");
            if (elements.get(elements.size() - 3).text().contains("BAR")) {
                isBarRechnung = true;
            } else {
                final String text = doc.text();
                String zahlungsart = StringUtils.substring(text, StringUtils.indexOf(text, "Zahlungsart: ") + 13, text.length());
                final int indexOf = StringUtils.indexOf(zahlungsart, " ");
                if (indexOf > 0) {
                    zahlungsart = StringUtils.substring(zahlungsart, 0, indexOf);
                }
                isBarRechnung = "BAR".equals(zahlungsart);
                log.info("Rechnung: {}, besitzt als Zahlungsart nicht 'BAR'. Alternatives Auslesen der Zahlungsart ergab: {}", dateiname,
                        zahlungsart);
            }
        } catch (final IOException e) {
            log.error("Datei: '{}' kann nicht geparst werden: {}", dateiname, e.getMessage());
        }
        return isBarRechnung;
    }

    private static Rechnung parseFile(final File file, final Date dateFrom, final Date dateTo) {
        return parseFile(file, dateFrom, dateTo, false);
    }

    private static Rechnung parseCompleteFile(final File file, final Date dateFrom, final Date dateTo) {
        return parseFile(file, dateFrom, dateTo, true);
    }

    private static Rechnung parseFile(final File file, final Date dateFrom, final Date dateTo, final boolean readPosten) {
        Rechnung rechnung = null;
        try {
            final Document doc = Jsoup.parse(file, Charsets.UTF_8.name());
            final Date rechnungsdatum = extractRechnungsdatumFromFile(doc, file.getName());
            if (isRechnungsdatumInRechnungszeitraum(dateFrom, dateTo, rechnungsdatum)) {
                rechnung = new Rechnung();
                rechnung.setRechnungsdatum(rechnungsdatum);
                rechnung.setRechnungsnummer(extractRechnungsnummerFromFile(doc, file.getName()));
                rechnung.setRechnungsbetrag(extractRechnungbetragFromFile(doc));
                if (readPosten) {
                    rechnung.setPosten(extractRechnungspostenFromFile(doc));
                }
                log.info("Erfolgreich eingelesene Rechnung: {}", rechnung);
            } else {
                log.info("Rechnungsdatum: {} der Rechnung {} liegt nicht im angegebenen Rechnunszeitraum von {} bis {}",
                        DATE_FORMAT.format(rechnungsdatum), file.getName(), DATE_FORMAT.format(dateFrom), DATE_FORMAT.format(dateTo));
            }
        } catch (final IOException e) {
            log.error("Datei: '{}' kann nicht geparst werden: {}", file.getName(), e.getMessage());
        } catch (final NumberFormatException e) {
            log.error("Rechnungsbetrag der Datei: '{}' kann nicht geparst werden: {}", file.getName(), e.getMessage());
        } catch (final ParseException e) {
            log.error("Rechnungsdatum: {} der Datei: '{}' kann nicht geparst werden: ", file.getName(), e.getMessage());
        }
        return rechnung;
    }

    private static boolean isRechnungsdatumInRechnungszeitraum(final Date dateFrom, final Date dateTo, final Date rechnungsdatum) {
        return rechnungsdatum.equals(dateFrom) || rechnungsdatum.equals(dateTo)
                || (rechnungsdatum.after(dateFrom) && rechnungsdatum.before(dateTo));
    }

    private static Zahlart extractZahlartFromFile(final Document doc) {
        final Elements elements = doc.select("p");
        final Zahlart art = Zahlart.getByBezeichnung(elements.get(elements.size() - 3).text());
        if (art == null) {
            final String text = doc.text();
            final String zahlungsart = StringUtils.substring(text, StringUtils.indexOf(text, "Zahlungsart: ") + 13, text.length());
            final int indexOf = StringUtils.indexOf(zahlungsart, " ");
            if (indexOf > 0) {
                Zahlart.getByBezeichnung(StringUtils.substring(zahlungsart, 0, indexOf));
            }
        }
        return art;
    }

    private static Date extractRechnungsdatumFromFile(final Document doc, final String filename) throws ParseException {
        Date date = null;
        try {
            final Elements elements = doc.select("table").get(0).select("td");
            final String rechnungsdatum = StringUtils.replace(elements.get(elements.size() - 1).text(), "Rechnungsdatum: ", "").trim();
            date = DateUtils.parseDate(rechnungsdatum, FORMAT_DATUM);
        } catch (final ParseException e) {
            final int indexOf = StringUtils.indexOf(doc.text(), "Rechnungsdatum: ");
            final String alternativeRechnungsdatum = StringUtils.substring(doc.text(), indexOf + 16, indexOf + 26);
            log.info(
                    "Rechnungsdatum: {} der Datei: '{}' kann nicht geparst werden. Alternativ ausgelesenes Rechnungsdatum wird verwendet: {}",
                    e.getMessage(), filename, alternativeRechnungsdatum);
            date = DateUtils.parseDate(alternativeRechnungsdatum, FORMAT_DATUM);
        }
        return date;
    }

    private static String extractRechnungsnummerFromFile(final Document doc, final String filename) {
        String rechnungsnummer = StringUtils.replace(doc.select("td").get(0).text(), "Rechnungsnummer: ", "");
        if (!StringUtils.isNumeric(rechnungsnummer)
                || (rechnungsnummer.length() >= 1 && !StringUtils.isNumeric(StringUtils.substring(rechnungsnummer, 1)))) {
            final String text = doc.text();
            StringUtils.indexOf(text, "Rechnungsnummer: ");
            StringUtils.indexOf(text, "Rechnungsdatum: ");
            final String alternativeRechnungsnummer = StringUtils
                    .substring(text, StringUtils.indexOf(text, "Rechnungsnummer: ") + 17, StringUtils.indexOf(text, "Rechnungsdatum: "))
                    .trim().replace("\u00a0", "");
            log.info("Rechnungsnummer: {} der Datei: '{}' ungültig. Alternativ ausgelesene Rechnungsnummer wird verwendet: {}",
                    rechnungsnummer, filename, alternativeRechnungsnummer);
            rechnungsnummer = alternativeRechnungsnummer;
        }
        return rechnungsnummer;
    }

    private static BigDecimal extractRechnungbetragFromFile(final Document doc) {
        final Elements tableElements = doc.select("table");
        final Elements elements = tableElements.get(tableElements.size() - 1).select("td");
        final String rechnungsbetrag = elements.get(elements.size() - 1).text();
        return new BigDecimal(normalizeCurrencyValue(rechnungsbetrag));
    }

    private static List<Rechnungsposten> extractRechnungspostenFromFile(final Document doc) {
        final List<Rechnungsposten> list = new ArrayList<>();
        final Elements tableElements = doc.select("table");
        final Elements elements = tableElements.get(tableElements.size() - 1).select("tr");
        for (int i = 0; i < elements.size(); i++) {
            if (i > 1 && i < elements.size() - 4) {
                final Rechnungsposten posten = createRechnungsposten(elements.get(i));
                if (posten != null) {
                    list.add(posten);
                }
            } else {
                log.debug("Überspringe Zeile: {}", elements.get(i).text());
            }
        }
        return list;
    }

    private static Rechnungsposten createRechnungsposten(final Element row) {
        final Elements columns = row.getElementsByTag("td");
        Rechnungsposten posten = null;
        if (columns.size() == 5) {
            posten = new Rechnungsposten(Ints.tryParse(columns.get(0).text()), columns.get(1).text(),
                    new BigDecimal(normalizeCurrencyValue(columns.get(2).text())),
                    new BigDecimal(normalizeCurrencyValue(columns.get(3).text())));
        } else {
            log.debug("Überspringe Posten, da ungültige Anzahl an Spalten: {}", row.text());
        }
        return posten;
    }

    private static String normalizeCurrencyValue(final String value) {
        return StringUtils.replace(StringUtils.substring(value, 0, value.length() - 1), ",", ".");
    }

}
