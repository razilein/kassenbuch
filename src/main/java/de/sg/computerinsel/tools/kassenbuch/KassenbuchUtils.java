package de.sg.computerinsel.tools.kassenbuch;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Sita Geßner
 */
public final class KassenbuchUtils {

    private KassenbuchUtils() {
    }

    public static String getAusgangsbetragFromLatestKassenbuch(final String rechnungsverzeichnis, final String ablageverzeichnis) {
        String betrag = StringUtils.EMPTY;
        final Properties settings = SettingsUtils.loadSettings();
        if (settings != null) {
            betrag = settings.getProperty(SettingsUtils.PROP_AUSGANGSBETRAG);
        } else {
            betrag = KassenstandBerechnenUtils.getGesamtbetragKassenbuch(rechnungsverzeichnis, ablageverzeichnis);
        }
        return betrag;
    }

    /**
     * Returns the path where the currently running JAR-file is located. Example value: C:\MyProject\build\jar\
     *
     * @return Path of the JAR-file
     */
    public static String getJarExecutionDirectory() {
        String jarFile = null;
        String jarDirectory = null;
        int cutFileSeperator = 0;
        int cutSemicolon = -1;

        jarFile = System.getProperty("java.class.path");
        // Cut seperators
        cutFileSeperator = jarFile.lastIndexOf(System.getProperty("file.separator"));
        jarDirectory = jarFile.substring(0, cutFileSeperator);
        // Cut semicolons
        cutSemicolon = jarDirectory.lastIndexOf(';');
        jarDirectory = jarDirectory.substring(cutSemicolon + 1, jarDirectory.length());

        return jarDirectory + System.getProperty("file.separator");
    }

}
