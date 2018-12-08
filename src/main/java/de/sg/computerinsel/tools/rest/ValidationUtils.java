package de.sg.computerinsel.tools.rest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public class ValidationUtils {

    public static <T> Map<String, Object> validate(final T obj) {
        final Map<String, Object> messages = new HashMap<>();

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        for (final ConstraintViolation<Object> constraintViolation : violations) {
            messages.put(Message.ERROR.getCode(), constraintViolation.getMessage());
        }
        return messages;
    }

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

    public static Map<String, Object> validateVerzeichnisse(final String rechnungsverzeichnis, final String ablageverzeichnis) {
        final Map<String, Object> messages = new HashMap<>();

        String message = ValidationUtils.validatePath(rechnungsverzeichnis, "Rechnungsverzeichnis");
        if (StringUtils.isNotBlank(message)) {
            messages.put(Message.ERROR.getCode(), message);
        }

        message = ValidationUtils.validatePath(ablageverzeichnis, "Ablageverzeichnis");
        if (StringUtils.isNotBlank(message)) {
            messages.put(Message.ERROR.getCode(), message);
        }
        return messages;
    }

}
