package de.sg.computerinsel.tools.kassenbuch.service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import lombok.experimental.UtilityClass;

/**
 * @author Sita Geßner
 */
@UtilityClass
public class PdfDocumentUtils {

    private static final Font TITLEFONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    public static void addTitle(final Document document, final String text) throws DocumentException {
        document.add(new Paragraph(text, TITLEFONT));
        addEmptyLine(document);
    }

    public static void addEmptyLine(final Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
    }

}
