package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;

/**
 * @author Sita Geßner
 */
public abstract class BaseKassenbuchGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EinstellungenGUI.class);

    public abstract JPanel createPanel();

    protected ActionListener getActionListenerBtnAnzeigen(final Einstellungen einstellungen) {
        return e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    final File file = new File(FilenameUtils.removeExtension(einstellungen.getDateipfadText()) + ".pdf");
                    if (file.exists()) {
                        Desktop.getDesktop().open(file);
                    }
                } catch (final IOException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }

        };
    }

}
