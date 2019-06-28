package de.sg.computerinsel.tools.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.rest.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class ValidationService {

    private final MessageService messageService;

    public <T> Map<String, String> validate(final T obj) {
        final Map<String, String> messages = validateObj(obj);
        for (final Entry<String, String> message : messages.entrySet()) {
            try {
                message.setValue(messageService.get(message.getValue()));
            } catch (final NoSuchMessageException e) {
                log.debug(e.getMessage(), e);
            }
        }
        return messages;
    }

    private static <T> Map<String, String> validateObj(final T obj) {
        final Map<String, String> messages = new HashMap<>();

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        for (final ConstraintViolation<Object> constraintViolation : violations) {
            messages.put(Message.ERROR.getCode(), constraintViolation.getMessage());
        }
        return messages;
    }

}
