package de.sg.computerinsel.tools.rest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public class ValidationUtils {

    public static String validatePath(final String path, final String messageDescription) {
        String result = null;
        if (StringUtils.isBlank(path) || !new File(path).exists() || !new File(path).isDirectory()) {
            result = "Bitte geben Sie ein gültiges " + messageDescription + " ein.";
            log.info("Ungültiges {} '{}'", messageDescription, path);
        } else if (StringUtils.isNoneBlank(path) && !new File(path).canWrite()) {
            result = "Auf das angegebene " + messageDescription + " besitzen Sie keine Schreibrechte.";
            log.info("Keine Schreibrechte auf {} '{}'", messageDescription, path);
        }
        return result;
    }

    public static Map<String, Object> validateVerzeichnisse(final String ablageverzeichnis) {
        final Map<String, Object> messages = new HashMap<>();

        final String message = ValidationUtils.validatePath(ablageverzeichnis, "Ablageverzeichnis");
        if (StringUtils.isNotBlank(message)) {
            messages.put(Message.ERROR.getCode(), message);
        }
        return messages;
    }

}
