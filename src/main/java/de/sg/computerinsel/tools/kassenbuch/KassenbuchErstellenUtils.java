package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.sg.computerinsel.tools.CurrencyUtils;
import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import de.sg.computerinsel.tools.kassenbuch.service.PdfDocumentUtils;
import de.sg.computerinsel.tools.kassenbuch.service.PdfTableUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public final class KassenbuchErstellenUtils {

    private static final String FILENAME_CSV = "kassenbuch.csv";

    private static final String FILENAME_PDF = "kassenbuch.pdf";

    public static File createCsv(final List<Rechnung> files, final Rechnung ausgangsRechnung, final String ablageverzeichnis) {
        final File csvFile = new File(ablageverzeichnis, DateUtils.nowDatetime() + FILENAME_CSV);
        final List<Rechnung> rechnungen = new ArrayList<>(files);
        rechnungen.add(0, ausgangsRechnung);
        Collections.sort(rechnungen, rechnungComparator());
        try (final FileWriter writer = new FileWriter(csvFile.getAbsoluteFile())) {
            BigDecimal gesamtBetrag = BigDecimal.ZERO;
            BigDecimal gesamtEingang = BigDecimal.ZERO;
            BigDecimal gesamtAusgang = BigDecimal.ZERO;
            LocalDate rechnungsdatum = null;
            for (final Rechnung rechnung : rechnungen) {
                if (rechnungsdatum != null && !rechnungsdatum.equals(rechnung.getRechnungsdatumWithoutTime())) {
                    writer.append("\r\n");
                    final String betrag = CurrencyUtils.format(gesamtBetrag);
                    writeCsvLine(writer, DateUtils.convert(rechnungsdatum), Rechnung.GESAMTBETRAG, gesamtEingang, gesamtAusgang, betrag);
                    writer.append("\r\n");
                    writeCsvLine(writer, rechnung.getRechnungsdatum(), Rechnung.AUSGANGSBETRAG, gesamtEingang, gesamtAusgang, betrag);
                }
                gesamtBetrag = gesamtBetrag.add(rechnung.getRechnungsbetrag());
                final String formattedRechnungsbetrag = CurrencyUtils.format(rechnung.getRechnungsbetrag());
                gesamtEingang = isEingangssbetrag(formattedRechnungsbetrag) ? gesamtEingang.add(rechnung.getRechnungsbetrag())
                        : gesamtEingang;
                gesamtAusgang = isAusgangsbetrag(formattedRechnungsbetrag) ? gesamtAusgang.add(rechnung.getRechnungsbetrag())
                        : gesamtAusgang;

                writer.append(rechnung.toCsvString(gesamtBetrag));
                rechnungsdatum = rechnung.getRechnungsdatumWithoutTime();
            }
            writer.append("\r\n");
            final String formattedGesamtbetrag = CurrencyUtils.format(gesamtBetrag);
            writeCsvLine(writer, null, Rechnung.GESAMTBETRAG, gesamtEingang, gesamtAusgang, formattedGesamtbetrag);
            writer.flush();
            log.info("CSV-Datei {} erfolgreich unter {} gespeichert.", csvFile.getName(), ablageverzeichnis);
        } catch (final IOException e) {
            log.error("Fehler beim Schreiben der CSV-Datei {}: {}", csvFile.getName(), e.getMessage());
        }
        return csvFile;
    }

    private static void writeCsvLine(final FileWriter writer, final Date rechnungsdatum, final String verwendungszweck,
            final BigDecimal gesamtEingang, final BigDecimal gesamtAusgang, final String betrag) throws IOException {
        writer.append(rechnungsdatum == null ? StringUtils.EMPTY : DateUtils.format(rechnungsdatum));
        writer.append(";");
        writer.append(verwendungszweck);
        writer.append(";");
        writer.append(CurrencyUtils.format(gesamtEingang));
        writer.append(";");
        writer.append(CurrencyUtils.format(gesamtAusgang));
        writer.append(";");
        writer.append(betrag);
        writer.append("\r\n");
    }

    public static Comparator<Rechnung> rechnungComparator() {
        return (o1, o2) -> {
            int result = 0;
            if (o1.getRechnungsdatumWithoutTime() != null && o2.getRechnungsdatumWithoutTime() != null) {
                result += o1.getRechnungsdatumWithoutTime().compareTo(o2.getRechnungsdatumWithoutTime());
            }
            if (o1.getRechnungsnummerAsInt() != null && o2.getRechnungsnummerAsInt() != null) {
                result += o1.getRechnungsnummerAsInt().compareTo(o2.getRechnungsnummerAsInt());
            }
            return result;
        };
    }

    public static File createPdf(final List<Rechnung> files, final Rechnung ausgangsRechnung, final String ablageverzeichnis) {
        final File pdfFile = new File(ablageverzeichnis, DateUtils.nowDatetime() + FILENAME_PDF);
        final Map<LocalDate, List<Rechnung>> rechnungenByRechnungsdatum = new ArrayList<>(files).stream()
                .filter(r -> r.getRechnungsdatum() != null)
                .collect(Collectors.groupingBy(Rechnung::getRechnungsdatumWithoutTime, LinkedHashMap::new, Collectors.toList()));
        try {
            if (pdfFile.createNewFile()) {
                log.debug("Neue PDF-Datei erzeugt: {}", pdfFile.getAbsolutePath());
            }
            try (final FileOutputStream outputStream = new FileOutputStream(pdfFile);) {
                final Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();

                BigDecimal ausgangsBetrag = ausgangsRechnung.getRechnungsbetrag();
                for (final Entry<LocalDate, List<Rechnung>> rechnungenByDatum : rechnungenByRechnungsdatum.entrySet()) {
                    PdfDocumentUtils.addTitle(document, "Kassenbuch vom " + DateUtils.format(rechnungenByDatum.getKey()));
                    final List<Rechnung> value = rechnungenByDatum.getValue();
                    Collections.sort(value, rechnungComparator());
                    ausgangsBetrag = addTable(document, value, ausgangsBetrag);
                    document.newPage();
                }
                document.close();
            }
        } catch (final DocumentException e) {
            log.error("Fehler beim Schreiben der PDF-Datei {}: {}", pdfFile.getName(), e.getMessage());
        } catch (final FileNotFoundException e) {
            log.error("PDF-Datei {} konnte nicht gefunden werden: {}", pdfFile.getName(), e.getMessage());
        } catch (final IOException e) {
            log.error("Fehler beim Erzeugen der PDF-Datei {}: {}", pdfFile.getName(), e.getMessage());
        }
        return pdfFile;
    }

    private static BigDecimal addTable(final Document document, final List<Rechnung> rechnungen, final BigDecimal ausgangsBetrag)
            throws DocumentException {
        final PdfPTable table = PdfTableUtils.createTable(5);
        addTableHeaderRow(table);
        final BigDecimal gesamtBetrag = addTableBodyRows(rechnungen, table, ausgangsBetrag);
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
        }

        for (final Rechnung rechnung : rechnungen) {
            final String verwendungszweck = StringUtils.isNumeric(rechnung.getRechnungsnummer())
                    || StringUtils.isNumeric(StringUtils.substring(rechnung.getRechnungsnummer(), 1))
                            ? "Rechnung: " + rechnung.getRechnungsnummer()
                            : rechnung.getRechnungsnummer();
            final String formattedRechnungsbetrag = CurrencyUtils.format(rechnung.getRechnungsbetrag());
            gesamtBetrag = gesamtBetrag.add(rechnung.getRechnungsbetrag());
            gesamtEingang = isEingangssbetrag(formattedRechnungsbetrag) ? gesamtEingang.add(rechnung.getRechnungsbetrag()) : gesamtEingang;
            gesamtAusgang = isAusgangsbetrag(formattedRechnungsbetrag) ? gesamtAusgang.add(rechnung.getRechnungsbetrag()) : gesamtAusgang;

            addTableRow(table, rechnung.getRechnungsdatum(), verwendungszweck,
                    isEingangssbetrag(formattedRechnungsbetrag) ? formattedRechnungsbetrag : StringUtils.EMPTY,
                    isAusgangsbetrag(formattedRechnungsbetrag) ? formattedRechnungsbetrag : StringUtils.EMPTY, gesamtBetrag);
        }
        PdfTableUtils.addEmptyTableRow(table);
        addTableRow(table, null, Rechnung.GESAMTBETRAG, CurrencyUtils.format(gesamtEingang), CurrencyUtils.format(gesamtAusgang),
                gesamtBetrag);
        return gesamtBetrag;
    }

    private static void addTableHeaderRow(final PdfPTable table) {
        PdfTableUtils.addTableHeaderCell(table, "Datum");
        PdfTableUtils.addTableHeaderCell(table, "Verwendungszweck");
        PdfTableUtils.addTableHeaderCell(table, "Einnahmen", Element.ALIGN_RIGHT);
        PdfTableUtils.addTableHeaderCell(table, "Ausgaben", Element.ALIGN_RIGHT);
        PdfTableUtils.addTableHeaderCell(table, "Kassenbestand", Element.ALIGN_RIGHT);
    }

    static boolean isAusgangsbetrag(final String formattedRechnungsbetrag) {
        return StringUtils.startsWith(formattedRechnungsbetrag, "-");
    }

    static boolean isEingangssbetrag(final String formattedRechnungsbetrag) {
        return !isAusgangsbetrag(formattedRechnungsbetrag);
    }

    private static void addTableRow(final PdfPTable table, final Date rechnungsdatum, final String verwendungszweck,
            final String betragEingang, final String betragAusgang, final BigDecimal gesamtBetrag) {
        PdfTableUtils.addTableBodyCell(table, rechnungsdatum == null ? StringUtils.EMPTY : DateUtils.format(rechnungsdatum));
        PdfTableUtils.addTableBodyCell(table, verwendungszweck);
        PdfTableUtils.addTableBodyCell(table, betragEingang, Element.ALIGN_RIGHT);
        PdfTableUtils.addTableBodyCell(table, betragAusgang, Element.ALIGN_RIGHT);
        PdfTableUtils.addTableBodyCell(table, CurrencyUtils.format(gesamtBetrag), Element.ALIGN_RIGHT);
    }

}
