package de.sg.computerinsel.tools.kassenbuch.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.UtilityClass;

/**
 * @author Sita Geßner
 */
@UtilityClass
public class PdfTableUtils {

    private static final Font TABLEHEADER_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);

    private static final Font TEXTFONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    @Data
    @AllArgsConstructor
    public class PdfCellElement {

        private String text;

        private int alignment = Element.ALIGN_LEFT;

        public PdfCellElement(final String text) {
            this.text = text;
        }

    }

    public static PdfPTable createTable(final int spalten) {
        final PdfPTable table = new PdfPTable(spalten);
        table.setHeaderRows(1);
        return table;
    }

    public static void addTableHeader(final PdfPTable table, final List<PdfCellElement> cells) {
        for (final PdfCellElement cell : cells) {
            table.addCell(createHeaderCell(cell.getText(), cell.getAlignment()));
        }
    }

    public static void addTableHeaderCell(final PdfPTable table, final String text, final int alignment) {
        table.addCell(createHeaderCell(text, alignment));
    }

    public static void addTableHeaderCell(final PdfPTable table, final String text) {
        table.addCell(createHeaderCell(text, Element.ALIGN_LEFT));
    }

    private static PdfPCell createHeaderCell(final String title, final int alignment) {
        final PdfPCell cell = new PdfPCell(new Phrase(title, TABLEHEADER_FONT));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    public static void addTableBody(final PdfPTable table, final List<PdfCellElement> cells) {
        for (final PdfCellElement cell : cells) {
            table.addCell(createBodyCell(cell.getText(), cell.getAlignment()));
        }
    }

    public static void addTableBodyCell(final PdfPTable table, final String text, final int alignment) {
        table.addCell(createBodyCell(text, alignment));
    }

    public static void addTableBodyCell(final PdfPTable table, final String text) {
        table.addCell(createBodyCell(text, Element.ALIGN_LEFT));
    }

    public static void addEmptyTableRow(final PdfPTable table, final int spalten) {
        for (int i = 0; i < 5; i++) {
            table.addCell(StringUtils.EMPTY);
        }
    }

    private static PdfPCell createBodyCell(final String text, final int alignment) {
        final PdfPCell cell = new PdfPCell(new Phrase(text, TEXTFONT));
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

}
