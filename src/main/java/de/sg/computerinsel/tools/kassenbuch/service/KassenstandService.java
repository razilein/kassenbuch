package de.sg.computerinsel.tools.kassenbuch.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.sg.computerinsel.tools.kassenbuch.KassenbuchErstellenUtils;
import de.sg.computerinsel.tools.kassenbuch.rest.model.Kassenstand;
import de.sg.computerinsel.tools.kassenbuch.service.PdfTableUtils.PdfCellElement;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Service
@Slf4j
@AllArgsConstructor
public class KassenstandService {

    private static final List<PdfCellElement> PDF_HEADER_CELLS = Arrays.asList(new PdfCellElement("Anzahl", Element.ALIGN_CENTER),
            new PdfCellElement("Wert", Element.ALIGN_RIGHT), new PdfCellElement("Betrag", Element.ALIGN_RIGHT));

    private static final String FILENAME_PDF = "kassenstand.pdf";

    private final EinstellungenService einstellungenService;

    public void ablegen(final List<Kassenstand> kassenstaende) {
        final File pdfFile = new File(einstellungenService.getAblageverzeichnis().getWert(),
                KassenbuchErstellenUtils.DATE_FORMAT_FILES.format(new Date()) + FILENAME_PDF);
        try {
            pdfFile.createNewFile();
            try (final FileOutputStream outputStream = new FileOutputStream(pdfFile);) {
                final com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();
                PdfDocumentUtils.addTitle(document, "Kassenstand vom " + KassenbuchErstellenUtils.DATE_FORMAT.format(new Date()));
                document.add(createTable(kassenstaende));
                document.close();
            }
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
        } catch (final DocumentException e) {
            log.error(e.getMessage(), e);
        }
    }

    private PdfPTable createTable(final List<Kassenstand> kassenstaende) {
        final PdfPTable table = PdfTableUtils.createTable(3);
        PdfTableUtils.addTableHeader(table, PDF_HEADER_CELLS);
        PdfTableUtils.addTableBody(table, getTaleBodyCells(kassenstaende));
        return table;
    }

    private List<PdfCellElement> getTaleBodyCells(final List<Kassenstand> kassenstaende) {
        final List<PdfCellElement> cells = new ArrayList<>();
        for (final Kassenstand kassenstand : kassenstaende) {
            cells.add(new PdfCellElement(String.valueOf(kassenstand.getAnzahl()), Element.ALIGN_CENTER));
            cells.add(new PdfCellElement(KassenbuchErstellenUtils.BETRAG_FORMAT.format(kassenstand.getMulti()), Element.ALIGN_RIGHT));
            cells.add(new PdfCellElement(KassenbuchErstellenUtils.BETRAG_FORMAT.format(kassenstand.getBetrag()), Element.ALIGN_RIGHT));
        }
        return cells;
    }

}
