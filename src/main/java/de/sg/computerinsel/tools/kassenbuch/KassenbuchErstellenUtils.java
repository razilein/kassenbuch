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
import java.util.List;

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
	
	private static final String FORMAT_DATUM = "dd.MM.yyyy";
	
	private static final String FORMAT_BETRAG = "#,###,##0.00";
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT_DATUM);

	private final static Logger LOGGER = LoggerFactory.getLogger(KassenbuchErstellenUtils.class);

	private static final String FILENAME_CSV = "kassenbuch.csv";

	private static final String FILENAME_PDF = "kassenbuch.pdf";

	private static final Font TITLEFONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
	
	private static final Font TEXTFONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

	private KassenbuchErstellenUtils() {
	}
	
	public static List<Rechnung> readHtmlFiles(final File directory, final Date dateFrom, final Date dateTo) {
		final File[] files = directory.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.contains(".htm");
			}
		});
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
		final File csvFile = new File(ablageverzeichnis, new SimpleDateFormat(FORMAT_DATUM + "_HH-mm-ss").format(new Date()) + FILENAME_CSV);
		final List<Rechnung> rechnungen = new ArrayList<>(files);
		Collections.sort(rechnungen, rechnungComparator());
		rechnungen.add(0, ausgangsRechnung);
		try {
			final FileWriter writer = new FileWriter(csvFile.getAbsoluteFile());
			BigDecimal gesamtBetrag = BigDecimal.ZERO;
			Date rechnungsdatum = null;
			for (final Rechnung rechnung : rechnungen) {
				if (rechnungsdatum != null && !rechnungsdatum.equals(rechnung.getRechnungsdatum())) {
					writer.append(KassenbuchErstellenUtils.DATE_FORMAT.format(rechnungsdatum));
					writer.append(";Gesamtbetrag;");
					final String betrag = new DecimalFormat(FORMAT_BETRAG).format(gesamtBetrag);
					writer.append(betrag);
					writer.append("\r\n");
					writer.append(KassenbuchErstellenUtils.DATE_FORMAT.format(rechnung.getRechnungsdatum()));
					writer.append(";Ausgangsbetrag;");
					writer.append(betrag);
					writer.append("\r\n");
				}
				writer.append(rechnung.toCsvString());
				gesamtBetrag = gesamtBetrag.add(rechnung.getRechnungsbetrag());
				rechnungsdatum = rechnung.getRechnungsdatum();
			}
			writer.append("\r\n");
			writer.append(";Gesamtbetrag;");
			writer.append(new DecimalFormat("#0.00").format(gesamtBetrag));
			writer.flush();
			writer.close();
			LOGGER.info("CSV-Datei {} erfolgreich unter {} gespeichert.", csvFile.getName(), ablageverzeichnis);
		} catch (final IOException e) {
			LOGGER.error("Fehler beim Schreiben der CSV-Datei {}: {}", csvFile.getName(), e.getMessage());
		}
		return csvFile;
	}
	
	private static Comparator<Rechnung> rechnungComparator() {
		return new Comparator<Rechnung>() {
			
			@Override
			public int compare(final Rechnung o1, final Rechnung o2) {
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
			}
		};
	}

	public static File createPdf(final List<Rechnung> files, final Rechnung ausgangsRechnung, final String ablageverzeichnis) {
		final File pdfFile = new File(ablageverzeichnis, new SimpleDateFormat(FORMAT_DATUM + "_HH-mm-ss").format(new Date()) + FILENAME_PDF);
		final List<Rechnung> rechnungen = new ArrayList<>(files);
		Collections.sort(rechnungen, rechnungComparator());
		rechnungen.add(0, ausgangsRechnung);
		try {
			pdfFile.createNewFile();
			final FileOutputStream outputStream = new FileOutputStream(pdfFile);
			final com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			PdfWriter.getInstance(document, outputStream);
			document.open();
			addTitlePage(document);
			addTable(document, rechnungen);
			document.close();
		} catch (final DocumentException e) {
			LOGGER.error("Fehler beim Schreiben der PDF-Datei {}: {}", pdfFile.getName(), e.getMessage());
		} catch (final FileNotFoundException e) {
			LOGGER.error("PDF-Datei {} konnte nicht gefunden werden: {}", pdfFile.getName(), e.getMessage());
		} catch (final IOException e) {
			LOGGER.error("Fehler beim Erzeugen der PDF-Datei {}: {}", pdfFile.getName(), e.getMessage());
		}
		return pdfFile;
	}
	
	private static void addTitlePage(final com.itextpdf.text.Document document) throws DocumentException {
		final Paragraph preface = new Paragraph();
		addEmptyLine(document);
		preface.add(new Paragraph("Kassenbuch", TITLEFONT));
		addEmptyLine(document);
		preface.add(new Paragraph("Erstellt am: " + DATE_FORMAT.format(new Date()), TEXTFONT));
		addEmptyLine(document);
		addEmptyLine(document);
		addEmptyLine(document);
		document.add(preface);
	}
	
	private static void addEmptyLine(final com.itextpdf.text.Document document) throws DocumentException {
		document.add(Chunk.NEWLINE);
	}

	private static void addTable(final com.itextpdf.text.Document document, final List<Rechnung> rechnungen) throws DocumentException {
		final PdfPTable table = new PdfPTable(3);
		
		PdfPCell c1 = new PdfPCell(new Phrase("Datum"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Verwendungszweck"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Betrag (in Euro)"));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBackgroundColor(BaseColor.GRAY);
		table.addCell(c1);
		table.setHeaderRows(1);

		BigDecimal gesamtBetrag = BigDecimal.ZERO;
		for (final Rechnung rechnung : rechnungen) {
			table.addCell(rechnung.getRechnungsdatum() == null ? StringUtils.EMPTY : DATE_FORMAT.format(rechnung.getRechnungsdatum()));
			table.addCell(StringUtils.isNumeric(rechnung.getRechnungsnummer())
			        || StringUtils.isNumeric(StringUtils.substring(rechnung.getRechnungsnummer(), 1)) ? "Rechnung: "
			        + rechnung.getRechnungsnummer() : rechnung.getRechnungsnummer());
			table.addCell(new DecimalFormat("#0.00").format(rechnung.getRechnungsbetrag()));
			gesamtBetrag = gesamtBetrag.add(rechnung.getRechnungsbetrag());
		}
		table.addCell("");
		table.addCell("Gesamtbetrag");
		table.addCell(new DecimalFormat("#0.00").format(gesamtBetrag));
		
		document.add(table);
		
	}
}
