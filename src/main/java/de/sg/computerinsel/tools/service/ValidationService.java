package de.sg.computerinsel.tools.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.apache.commons.lang3.StringUtils;
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

    public <T> Map<String, String> validate(final T obj, final Class<?>... validationGroups) {
        final String messages = validateObj(obj, validationGroups).entrySet().stream().map(Entry::getValue).flatMap(List::stream)
                .collect(Collectors.joining("<br>"));

        final Map<String, String> result = new HashMap<>();
        if (StringUtils.isNotBlank(messages)) {
            result.put(Message.ERROR.getCode(), messages);
        }
        return result;
    }

    private <T> Map<String, List<String>> validateObj(final T obj, final Class<?>... validationGroups) {
        final Map<String, List<String>> messages = new HashMap<>();

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<Object>> violations = validationGroups.length == 0 ? validator.validate(obj, Default.class)
                : validator.validate(obj, validationGroups);
        violations.forEach(v -> {
            final String key = v.getLeafBean().toString();
            final List<String> list = getMessagesByKey(messages, key);
            list.add(getMessage(v));
            messages.put(key, list);
        });
        return messages;
    }

    private List<String> getMessagesByKey(final Map<String, List<String>> messages, final String key) {
        List<String> list = messages.get(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    private String getMessage(final ConstraintViolation<Object> violation) {
        String message = violation.getMessage();
        try {
            message = messageService.get(message);
        } catch (final NoSuchMessageException e) {
            log.debug(e.getMessage(), e);
        }
        return message;
    }

}
