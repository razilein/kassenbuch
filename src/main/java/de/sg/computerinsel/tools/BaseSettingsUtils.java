package de.sg.computerinsel.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.kassenbuch.SettingsUtils;

public class BaseSettingsUtils {

    private static final String FOLDER_CONFIG_PROPERTIES = "settings";

    protected static final String FILENAME_CONFIG_PROPERTIES = "config.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsUtils.class);

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

    public static File getPropertyFile() {
        return new File(FilenameUtils.concat(FOLDER_CONFIG_PROPERTIES, FILENAME_CONFIG_PROPERTIES));
    }

    protected static void saveProperties(final Properties props) {
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

}
