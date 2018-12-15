package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public final class RechnungenEinlesenUtils {

    private static final String ELEMENT_TABLE = "table";

    private static final String RECHNUNGSDATUM = "Rechnungsdatum: ";

    private static final String RECHNUNGSNUMMER = "Rechnungsnummer: ";

    public static List<Rechnung> readHtmlFiles(final File directory, final Date dateFrom, final Date dateTo) {
        return readHtmlFiles(directory, dateFrom, dateTo, false);
    }

    public static List<Rechnung> readHtmlFilesWithPosten(final File directory, final Date dateFrom, final Date dateTo) {
        return readHtmlFiles(directory, dateFrom, dateTo, true);
    }

    private static List<Rechnung> readHtmlFiles(final File directory, final Date dateFrom, final Date dateTo, final boolean complete) {
        final Collection<File> files = listFiles(directory, complete);
        log.info("{} Rechnungen gefunden", files.size());

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

    private Collection<File> listFiles(final File directory, final boolean complete) {
        return FileUtils.listFiles(directory, null, complete);
    }

    private static boolean isBarRechnung(final File file) {
        Boolean isBarRechnung = false;
        final String dateiname = file.getName();
        try {
            final Document doc = Jsoup.parse(file, StandardCharsets.UTF_8.name());
            isBarRechnung = Zahlart.BAR == extractZahlartFromFile(doc);
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
            final Document doc = Jsoup.parse(file, StandardCharsets.UTF_8.name());
            final Date rechnungsdatum = extractRechnungsdatumFromFile(doc, file.getName());
            if (isRechnungsdatumInRechnungszeitraum(dateFrom, dateTo, rechnungsdatum)) {
                rechnung = new Rechnung();
                rechnung.setRechnungsdatum(rechnungsdatum);
                rechnung.setRechnungsnummer(extractRechnungsnummerFromFile(doc, file.getName()));
                rechnung.setRechnungsbetrag(extractRechnungbetragFromFile(doc));
                if (readPosten) {
                    rechnung.setPosten(extractRechnungspostenFromFile(doc));
                    rechnung.setArt(extractZahlartFromFile(doc));
                    rechnung.setAdressfeld(extractAdressfeldFromFile(doc));
                }
                log.debug("Erfolgreich eingelesene Rechnung: {}", rechnung);
            } else {
                log.debug("Rechnungsdatum: {} der Rechnung {} liegt nicht im angegebenen Rechnunszeitraum von {} bis {}",
                        DateUtils.format(rechnungsdatum), file.getName(), DateUtils.format(dateFrom), DateUtils.format(dateTo));
            }
        } catch (final IOException e) {
            log.error("Datei: '{}' kann nicht geparst werden: {}", file.getName(), e.getMessage());
        } catch (final NumberFormatException e) {
            log.error("Rechnungsbetrag der Datei: '{}' kann nicht geparst werden: {}", file.getName(), e.getMessage());
        } catch (final DateTimeParseException e) {
            log.error("Rechnungsdatum: {} der Datei: '{}' kann nicht geparst werden: ", file.getName(), e.getMessage());
        }
        return rechnung;
    }

    private static String extractAdressfeldFromFile(final Document doc) {
        return doc.select("p").get(0).text();
    }

    private static boolean isRechnungsdatumInRechnungszeitraum(final Date dateFrom, final Date dateTo, final Date rechnungsdatum) {
        final LocalDate zeitraumVon = DateUtils.convert(dateFrom);
        final LocalDate zeitraumBis = DateUtils.convert(dateTo);

        final LocalDate datum = DateUtils.convert(rechnungsdatum);
        return datum.equals(zeitraumVon) || datum.equals(zeitraumBis) || (datum.isAfter(zeitraumVon) && datum.isBefore(zeitraumBis));
    }

    private static Zahlart extractZahlartFromFile(final Document doc) {
        final Elements elements = doc.select("p");
        Zahlart art = Zahlart.getByBezeichnung(elements.get(elements.size() - 3).text());
        if (art == null) {
            final String text = doc.text();
            final String zahlungsart = StringUtils.substring(text, StringUtils.indexOf(text, "Zahlungsart: ") + 13, text.length());
            final int indexOf = StringUtils.indexOf(zahlungsart, " ");
            if (indexOf > 0) {
                art = Zahlart.getByBezeichnung(StringUtils.substring(zahlungsart, 0, indexOf));
            }
        }
        return art;
    }

    private static Date extractRechnungsdatumFromFile(final Document doc, final String filename) {
        Date date = null;
        try {
            final Elements elements = doc.select(ELEMENT_TABLE).get(0).select("td");
            final String rechnungsdatum = StringUtils.replace(elements.get(elements.size() - 1).text(), RECHNUNGSDATUM, "").trim();
            date = DateUtils.parseDate(rechnungsdatum);
        } catch (final DateTimeParseException e) {
            final int indexOf = StringUtils.indexOf(doc.text(), RECHNUNGSDATUM);
            final String alternativeRechnungsdatum = StringUtils.substring(doc.text(), indexOf + 16, indexOf + 26);
            log.debug(
                    "Rechnungsdatum: {} der Datei: '{}' kann nicht geparst werden. Alternativ ausgelesenes Rechnungsdatum wird verwendet: {}",
                    e.getMessage(), filename, alternativeRechnungsdatum);
            date = DateUtils.parseDate(alternativeRechnungsdatum);
        }
        return date;
    }

    private static String extractRechnungsnummerFromFile(final Document doc, final String filename) {
        String rechnungsnummer = StringUtils.replace(doc.select("td").get(0).text(), RECHNUNGSNUMMER, "");
        if (!StringUtils.isNumeric(rechnungsnummer)
                || (rechnungsnummer.length() >= 1 && !StringUtils.isNumeric(StringUtils.substring(rechnungsnummer, 1)))) {
            final String text = doc.text();
            StringUtils.indexOf(text, RECHNUNGSNUMMER);
            StringUtils.indexOf(text, RECHNUNGSDATUM);
            final String alternativeRechnungsnummer = StringUtils
                    .substring(text, StringUtils.indexOf(text, RECHNUNGSNUMMER) + 17, StringUtils.indexOf(text, RECHNUNGSDATUM)).trim()
                    .replace("\u00a0", "");
            log.debug("Rechnungsnummer: {} der Datei: '{}' ungültig. Alternativ ausgelesene Rechnungsnummer wird verwendet: {}",
                    rechnungsnummer, filename, alternativeRechnungsnummer);
            rechnungsnummer = alternativeRechnungsnummer;
        }
        return rechnungsnummer;
    }

    private static BigDecimal extractRechnungbetragFromFile(final Document doc) {
        final Elements tableElements = doc.select(ELEMENT_TABLE);
        final Element sumElements = tableElements.get(tableElements.size() - 1);
        final Elements rows = sumElements.select("tr");
        final Element possibleBetragRow1 = rows.get(rows.size() - 1);
        final Element possibleBetragRow2 = rows.get(rows.size() - 3);

        final Element betragRow = possibleBetragRow1.text().contains("Rechnungsbetrag") ? possibleBetragRow1 : possibleBetragRow2;
        final Elements elements = betragRow.select("td");
        final String rechnungsbetrag = elements.get(elements.size() - 1).text();

        return new BigDecimal(normalizeCurrencyValue(rechnungsbetrag));
    }

    private static List<Rechnungsposten> extractRechnungspostenFromFile(final Document doc) {
        final List<Rechnungsposten> list = new ArrayList<>();
        final Elements tableElements = doc.select(ELEMENT_TABLE);
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
            try {
                posten = new Rechnungsposten(Ints.tryParse(columns.get(0).text()), normalizeBezeichnung(columns.get(1).text()),
                        new BigDecimal(normalizeCurrencyValue(columns.get(2).text())),
                        new BigDecimal(normalizeCurrencyValue(columns.get(3).text())),
                        new BigDecimal(normalizeCurrencyValue(columns.get(4).text())));
            } catch (final Exception e) {
                log.error("Posten ist ungültig und kann nicht ausgelsen werden: {}", columns.text(), e.getMessage(), e);
            }
        } else {
            log.debug("Überspringe Posten, da ungültige Anzahl an Spalten: {}", row.text());
        }
        return posten;
    }

    private static String normalizeBezeichnung(final String text) {
        final String normalized = text.endsWith("()") ? text.substring(0, text.length() - 2) : text;
        return normalized.trim();
    }

    private static String normalizeCurrencyValue(final String value) {
        return StringUtils.replace(StringUtils.substring(value, 0, value.length() - 1), ",", ".");
    }

}
