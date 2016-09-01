package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;

/**
 * @author Sita Geßner
 */
public final class KassenbuchErstellenUtils {

    public static final DecimalFormat BETRAG_FORMAT = new DecimalFormat("#,###,##0.00");

    private static final String FORMAT_DATUM = "dd.MM.yyyy";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT_DATUM);

    public static final SimpleDateFormat DATE_FORMAT_FILES = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private final static Logger LOGGER = LoggerFactory.getLogger(KassenbuchErstellenUtils.class);

    private static final String FILENAME_CSV = "kassenbuch.csv";

    private static final String FILENAME_PDF = "kassenbuch.pdf";

    private static final Font TITLEFONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    private static final Font TABLEHEADER_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);

    private static final Font TEXTFONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    private KassenbuchErstellenUtils() {
    }

    public static List<Rechnung> readHtmlFiles(final File directory, final Date dateFrom, final Date dateTo) {
        final File[] files = directory.listFiles((FilenameFilter) (dir, name) -> name.contains(".htm"));
        LOGGER.info("{} Rechnungen gefunden", files.length);

        int counterBarRechnungen = 0;
        final List<Rechnung> rechnungen = new ArrayList<>();
        for (final File file : files) {
            if (isBarRechnung(file)) {
                final Rechnung rechnung = parseFile(file, dateFrom, dateTo);
                if (rechnung != null) {
                    rechnungen.add(rechnung);
                    counterBarRechnungen++;
                }
            }
        }
        LOGGER.info("{} Rechnungen verarbeitet", counterBarRechnungen);
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
                LOGGER.info("Rechnung: {}, besitzt als Zahlungsart nicht 'BAR'. Alternatives Auslesen der Zahlungsart ergab: {}",
                        dateiname, zahlungsart);
            }
        } catch (final IOException e) {
            LOGGER.error("Datei: '{}' kann nicht geparst werden: {}", dateiname, e.getMessage());
        }
        return isBarRechnung;
    }

    private static Rechnung parseFile(final File file, final Date dateFrom, final Date dateTo) {
        Rechnung rechnung = null;
        try {
            final Document doc = Jsoup.parse(file, Charsets.UTF_8.name());
            final Date rechnungsdatum = extractRechnungsdatumFromFile(doc, file.getName());
            if (isRechnungsdatumInRechnungszeitraum(dateFrom, dateTo, rechnungsdatum)) {
                rechnung = new Rechnung();
                rechnung.setRechnungsdatum(rechnungsdatum);
                rechnung.setRechnungsnummer(extractRechnungsnummerFromFile(doc, file.getName()));
                rechnung.setRechnungsbetrag(extractRechnungbetragFromFile(doc));
                LOGGER.info("Erfolgreich eingelesene Rechnung: {}", rechnung);
            } else {
                LOGGER.info("Rechnungsdatum: {} der Rechnung {} liegt nicht im angegebenen Rechnunszeitraum von {} bis {}",
                        DATE_FORMAT.format(rechnungsdatum), file.getName(), DATE_FORMAT.format(dateFrom), DATE_FORMAT.format(dateTo));
            }
        } catch (final IOException e) {
            LOGGER.error("Datei: '{}' kann nicht geparst werden: {}", file.getName(), e.getMessage());
        } catch (final NumberFormatException e) {
            LOGGER.error("Rechnungsbetrag der Datei: '{}' kann nicht geparst werden: {}", file.getName(), e.getMessage());
        } catch (final ParseException e) {
            LOGGER.error("Rechnungsdatum: {} der Datei: '{}' kann nicht geparst werden: ", file.getName(), e.getMessage());
        }
        return rechnung;
    }

    private static boolean isRechnungsdatumInRechnungszeitraum(final Date dateFrom, final Date dateTo, final Date rechnungsdatum) {
        return rechnungsdatum.equals(dateFrom) || rechnungsdatum.equals(dateTo)
                || (rechnungsdatum.after(dateFrom) && rechnungsdatum.before(dateTo));
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
            LOGGER.info(
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
            LOGGER.info("Rechnungsnummer: {} der Datei: '{}' ungültig. Alternativ ausgelesene Rechnungsnummer wird verwendet: {}",
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

    private static String normalizeCurrencyValue(final String value) {
        return StringUtils.replace(StringUtils.substring(value, 0, value.length() - 1), ",", ".");
    }

    public static File createCsv(final List<Rechnung> files, final Rechnung ausgangsRechnung, final String ablageverzeichnis) {
        final File csvFile = new File(ablageverzeichnis, DATE_FORMAT_FILES.format(new Date()) + FILENAME_CSV);
        final List<Rechnung> rechnungen = new ArrayList<>(files);
        rechnungen.add(0, ausgangsRechnung);
        Collections.sort(rechnungen, rechnungComparator());
        try {
            final FileWriter writer = new FileWriter(csvFile.getAbsoluteFile());
            BigDecimal gesamtBetrag = BigDecimal.ZERO;
            BigDecimal gesamtEingang = BigDecimal.ZERO;
            BigDecimal gesamtAusgang = BigDecimal.ZERO;
            Date rechnungsdatum = null;
            for (final Rechnung rechnung : rechnungen) {
                if (rechnungsdatum != null && !rechnungsdatum.equals(rechnung.getRechnungsdatum())) {
                    writer.append("\r\n");
                    final String betrag = BETRAG_FORMAT.format(gesamtBetrag);
                    writeCsvLine(writer, rechnungsdatum, Rechnung.GESAMTBETRAG, gesamtEingang, gesamtAusgang, betrag);
                    writer.append("\r\n");
                    writeCsvLine(writer, rechnung.getRechnungsdatum(), Rechnung.AUSGANGSBETRAG, gesamtEingang, gesamtAusgang, betrag);
                }
                gesamtBetrag = gesamtBetrag.add(rechnung.getRechnungsbetrag());
                final String formattedRechnungsbetrag = BETRAG_FORMAT.format(rechnung.getRechnungsbetrag());
                gesamtEingang = isEingangssbetrag(formattedRechnungsbetrag) ? gesamtEingang.add(rechnung.getRechnungsbetrag())
                        : gesamtEingang;
                gesamtAusgang = isAusgangsbetrag(formattedRechnungsbetrag) ? gesamtAusgang.add(rechnung.getRechnungsbetrag())
                        : gesamtAusgang;

                writer.append(rechnung.toCsvString(gesamtBetrag));
                rechnungsdatum = rechnung.getRechnungsdatum();
            }
            writer.append("\r\n");
            final String formattedGesamtbetrag = BETRAG_FORMAT.format(gesamtBetrag);
            writeCsvLine(writer, null, Rechnung.GESAMTBETRAG, gesamtEingang, gesamtAusgang, formattedGesamtbetrag);
            writer.flush();
            writer.close();
            SettingsUtils.setPropertyAusgangsbetrag(formattedGesamtbetrag);
            LOGGER.info("CSV-Datei {} erfolgreich unter {} gespeichert.", csvFile.getName(), ablageverzeichnis);
        } catch (final IOException e) {
            LOGGER.error("Fehler beim Schreiben der CSV-Datei {}: {}", csvFile.getName(), e.getMessage());
        }
        return csvFile;
    }

    private static void writeCsvLine(final FileWriter writer, final Date rechnungsdatum, final String verwendungszweck,
            final BigDecimal gesamtEingang, final BigDecimal gesamtAusgang, final String betrag) throws IOException {
        writer.append(rechnungsdatum == null ? StringUtils.EMPTY : DATE_FORMAT.format(rechnungsdatum));
        writer.append(";");
        writer.append(verwendungszweck);
        writer.append(";");
        writer.append(BETRAG_FORMAT.format(gesamtEingang));
        writer.append(";");
        writer.append(BETRAG_FORMAT.format(gesamtAusgang));
        writer.append(";");
        writer.append(betrag);
        writer.append("\r\n");
    }

    private static Comparator<Rechnung> rechnungComparator() {
        return (o1, o2) -> {
            int result = 0;
            if (o1.getRechnungsdatum() == null) {
                result = o1.getRechnungsdatum() == null ? 0 : 1;
            }
            if (o2.getRechnungsdatum() == null) {
                result = o2.getRechnungsdatum() == null ? 0 : 1;
            }
            if (o1.getRechnungsdatum() != null && o2.getRechnungsdatum() != null) {
                result = o1.getRechnungsdatum().compareTo(o2.getRechnungsdatum());
            }
            if (o1.getRechnungsnummerAsInt() != null && o2.getRechnungsnummerAsInt() != null) {
                result += o1.getRechnungsnummerAsInt().compareTo(o2.getRechnungsnummerAsInt());
            }
            return result;
        };
    }

    public static File createPdf(final List<Rechnung> files, final Rechnung ausgangsRechnung, final String ablageverzeichnis) {
        final File pdfFile = new File(ablageverzeichnis, DATE_FORMAT_FILES.format(new Date()) + FILENAME_PDF);
        final List<Rechnung> rechnungen = new ArrayList<>(files);
        rechnungen.add(0, ausgangsRechnung);
        Collections.sort(rechnungen, rechnungComparator());
        final Map<Date, List<Rechnung>> rechnungenByRechnungsdatum = rechnungen.stream().filter(r -> r.getRechnungsdatum() != null)
                .collect(Collectors.groupingBy(Rechnung::getRechnungsdatum, LinkedHashMap::new, Collectors.toList()));
        try {
            pdfFile.createNewFile();
            try (final FileOutputStream outputStream = new FileOutputStream(pdfFile);) {
                final com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();

                BigDecimal ausgangsBetrag = null;
                for (final Entry<Date, List<Rechnung>> rechnungenByDatum : rechnungenByRechnungsdatum.entrySet()) {
                    addTitlePage(document, rechnungenByDatum.getKey());
                    ausgangsBetrag = addTable(document, rechnungenByDatum.getValue(), ausgangsBetrag);
                    document.newPage();
                }
                document.close();
            }
        } catch (final DocumentException e) {
            LOGGER.error("Fehler beim Schreiben der PDF-Datei {}: {}", pdfFile.getName(), e.getMessage());
        } catch (final FileNotFoundException e) {
            LOGGER.error("PDF-Datei {} konnte nicht gefunden werden: {}", pdfFile.getName(), e.getMessage());
        } catch (final IOException e) {
            LOGGER.error("Fehler beim Erzeugen der PDF-Datei {}: {}", pdfFile.getName(), e.getMessage());
        }
        return pdfFile;
    }

    private static void addTitlePage(final com.itextpdf.text.Document document, final Date datum) throws DocumentException {
        document.add(new Paragraph("Kassenbuch vom " + DATE_FORMAT.format(datum), TITLEFONT));
        addEmptyLine(document);
    }

    private static void addEmptyLine(final com.itextpdf.text.Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
    }

    private static BigDecimal addTable(final com.itextpdf.text.Document document, final List<Rechnung> rechnungen,
            final BigDecimal ausgangsBetrag) throws DocumentException {
        BigDecimal gesamtBetrag = BigDecimal.ZERO;
        final PdfPTable table = new PdfPTable(5);
        table.setHeaderRows(1);
        addTableHeaderRow(table);
        gesamtBetrag = addTableBodyRows(rechnungen, table, ausgangsBetrag);
        document.add(table);
        return gesamtBetrag;
    }

    private static BigDecimal addTableBodyRows(final List<Rechnung> rechnungen, final PdfPTable table, final BigDecimal ausgangsBetrag) {
        BigDecimal gesamtBetrag = BigDecimal.ZERO;
        BigDecimal gesamtEingang = BigDecimal.ZERO;
        BigDecimal gesamtAusgang = BigDecimal.ZERO;
        if (ausgangsBetrag != null) {
            addTableRow(table, rechnungen.isEmpty() ? null : rechnungen.get(0).getRechnungsdatum(), Rechnung.AUSGANGSBETRAG, null, null,
                    ausgangsBetrag);
            gesamtBetrag = ausgangsBetrag;
            gesamtEingang = ausgangsBetrag;
        }

        for (final Rechnung rechnung : rechnungen) {
            final String verwendungszweck = StringUtils.isNumeric(rechnung.getRechnungsnummer())
                    || StringUtils.isNumeric(StringUtils.substring(rechnung.getRechnungsnummer(), 1)) ? "Rechnung: "
                    + rechnung.getRechnungsnummer() : rechnung.getRechnungsnummer();
            final String formattedRechnungsbetrag = BETRAG_FORMAT.format(rechnung.getRechnungsbetrag());
            gesamtBetrag = gesamtBetrag.add(rechnung.getRechnungsbetrag());
            gesamtEingang = isEingangssbetrag(formattedRechnungsbetrag) ? gesamtEingang.add(rechnung.getRechnungsbetrag()) : gesamtEingang;
            gesamtAusgang = isAusgangsbetrag(formattedRechnungsbetrag) ? gesamtAusgang.add(rechnung.getRechnungsbetrag()) : gesamtAusgang;

            addTableRow(table, rechnung.getRechnungsdatum(), verwendungszweck,
                    isEingangssbetrag(formattedRechnungsbetrag) ? formattedRechnungsbetrag : StringUtils.EMPTY,
                    isAusgangsbetrag(formattedRechnungsbetrag) ? formattedRechnungsbetrag : StringUtils.EMPTY, gesamtBetrag);
        }
        addEmptyTableRow(table);
        addTableRow(table, null, Rechnung.GESAMTBETRAG, BETRAG_FORMAT.format(gesamtEingang), BETRAG_FORMAT.format(gesamtAusgang),
                gesamtBetrag);
        return gesamtBetrag;
    }

    private static void addTableHeaderRow(final PdfPTable table) {
        table.addCell(createHeaderCell("Datum"));
        table.addCell(createHeaderCell("Verwendungszweck"));
        table.addCell(createHeaderCell("Einnahmen", Element.ALIGN_RIGHT));
        table.addCell(createHeaderCell("Ausgaben", Element.ALIGN_RIGHT));
        table.addCell(createHeaderCell("Kassenbestand", Element.ALIGN_RIGHT));
    }

    private static PdfPCell createHeaderCell(final String title, final int alignment) {
        final PdfPCell cell = new PdfPCell(new Phrase(title, TABLEHEADER_FONT));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private static PdfPCell createHeaderCell(final String title) {
        return createHeaderCell(title, Element.ALIGN_LEFT);
    }

    private static PdfPCell createBodyCell(final String text, final int alignment) {
        final PdfPCell cell = new PdfPCell(new Phrase(text, TEXTFONT));
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private static PdfPCell createBodyCell(final String text) {
        return createBodyCell(text, Element.ALIGN_LEFT);
    }

    static boolean isAusgangsbetrag(final String formattedRechnungsbetrag) {
        return StringUtils.startsWith(formattedRechnungsbetrag, "-");
    }

    static boolean isEingangssbetrag(final String formattedRechnungsbetrag) {
        return !isAusgangsbetrag(formattedRechnungsbetrag);
    }

    private static void addEmptyTableRow(final PdfPTable table) {
        for (int i = 0; i < 5; i++) {
            table.addCell("");
        }
    }

    private static void addTableRow(final PdfPTable table, final Date rechnungsdatum, final String verwendungszweck,
            final String betragEingang, final String betragAusgang, final BigDecimal gesamtBetrag) {
        table.addCell(createBodyCell(rechnungsdatum == null ? StringUtils.EMPTY : DATE_FORMAT.format(rechnungsdatum)));
        table.addCell(createBodyCell(verwendungszweck));
        table.addCell(createBodyCell(betragEingang, Element.ALIGN_RIGHT));
        table.addCell(createBodyCell(betragAusgang, Element.ALIGN_RIGHT));
        table.addCell(createBodyCell(BETRAG_FORMAT.format(gesamtBetrag), Element.ALIGN_RIGHT));
    }
}
