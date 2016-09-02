package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sita Geßner
 */
public final class SettingsUtils {

    static final String PROP_AUSGANGSBETRAG = "kassenbuch.ausgangsbetrag";

    static final String PROP_ABLAGEVERZEICHNIS = "kassenbuch.ablageverzeichnis";

    static final String PROP_RECHNUNGSVERZEICHNIS = "kassenbuch.rechnungsverzeichnis";

    static final String PROP_LETZTE_CSV = "kassenbuch.letztecsvdateipfad";

    static final String PROP_KASSENSTAND = "kassenbuch.kassenstand";

    private static final String FOLDER_CONFIG_PROPERTIES = "settings";

    private static final String FILENAME_CONFIG_PROPERTIES = "config.properties";

    private final static Logger LOGGER = LoggerFactory.getLogger(SettingsUtils.class);

    private SettingsUtils() {
    }

    public static void saveSettings(final String rechnungsverzeichnis, final String ablageverzeichnis, final String ausgangsbetrag,
            final String letzteCsv) {
        final Properties props = setProperties(rechnungsverzeichnis, ablageverzeichnis, ausgangsbetrag, letzteCsv);
        saveProperties(props);
    }

    private static void saveProperties(final Properties props) {
        final File file = getPropertyFile();
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            try (FileWriter writer = new FileWriter(file);) {
                props.store(writer, "Einstellungen Kassenbuch");
                writer.close();
            } catch (final FileNotFoundException e) {
                LOGGER.error("Datei: {} konnte nicht gefunden werden.", FILENAME_CONFIG_PROPERTIES, e.getMessage());
            } catch (final IOException e) {
                LOGGER.error("Fehler beim Schreiben der Datei: {}.", FILENAME_CONFIG_PROPERTIES, e.getMessage());
            }
        } catch (final IOException e) {
            LOGGER.error("Fehler beim Erzeugen der Datei: {}.", FILENAME_CONFIG_PROPERTIES, e.getMessage());
        }
    }

    private static Properties setProperties(final String rechnungsverzeichnis, final String ablageverzeichnis, final String ausgangsbetrag,
            final String letzteCsv) {
        final Properties props = new Properties();
        props.setProperty(PROP_RECHNUNGSVERZEICHNIS, rechnungsverzeichnis);
        props.setProperty(PROP_ABLAGEVERZEICHNIS, ablageverzeichnis);
        props.setProperty(PROP_AUSGANGSBETRAG, ausgangsbetrag);
        props.setProperty(PROP_LETZTE_CSV, letzteCsv);
        return props;
    }

    public static void setPropertyLastCsvFile(final String pfadLetzteCsv) {
        final File file = getPropertyFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (final IOException e) {
                LOGGER.error("Fehler beim Schreiben der Datei: {}.", FILENAME_CONFIG_PROPERTIES, e.getMessage());
            }
            final Properties props = loadSettings();
            props.setProperty(PROP_LETZTE_CSV, pfadLetzteCsv);
            saveProperties(props);
        }
    }

    private static File getPropertyFile() {
        return new File(FilenameUtils.concat(FOLDER_CONFIG_PROPERTIES, FILENAME_CONFIG_PROPERTIES));
    }

    public static void setPropertyAusgangsbetrag(final String ausgangsbetrag) {
        final Properties props = loadSettings();
        props.setProperty(PROP_AUSGANGSBETRAG, ausgangsbetrag.replace(".", ""));
        saveProperties(props);
    }

    public static void setPropertyKassenstand(final String kassenstand) {
        final Properties props = loadSettings();
        props.setProperty(PROP_KASSENSTAND, kassenstand.replaceAll("[^0-9|]", ""));
        saveProperties(props);
    }

    public static Properties loadSettings() {
        Properties props = null;
        try (final FileReader reader = new FileReader(getPropertyFile())) {
            props = new Properties();
            props.load(reader);
        } catch (final FileNotFoundException e) {
            LOGGER.error("Datei: {} konnte nicht gefunden werden.", FILENAME_CONFIG_PROPERTIES, e.getMessage());
        } catch (final IOException e) {
            LOGGER.error("Fehler beim Schreiben der Datei: {}.", FILENAME_CONFIG_PROPERTIES, e.getMessage());
        }
        return props;
    }

}
