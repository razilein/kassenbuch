package de.sg.computerinsel.tools.reparatur;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.BaseSettingsUtils;

/**
 * @author Sita Geßner
 */
public final class SettingsUtils extends BaseSettingsUtils {

    static final String PROP_FILIALE = "reparatur.filiale";

    private SettingsUtils() {
    }

    public static void saveFiliale(final Integer id) {
        final Properties props = loadSettings();
        props.setProperty(PROP_FILIALE, id == null ? StringUtils.EMPTY : String.valueOf(id));
        saveProperties(props);
    }

    public static Integer getFiliale() {
        final Properties props = loadSettings();
        final String prop = (String) props.get(PROP_FILIALE);
        return prop == null || !StringUtils.isNumeric(prop) ? null : Integer.valueOf(prop);
    }
}
