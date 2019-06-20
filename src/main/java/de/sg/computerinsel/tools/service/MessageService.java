package de.sg.computerinsel.tools.service;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Resource
    private MessageSource messageSource;

    public String get(final String key) {
        try {
            return messageSource.getMessage(key, null, Locale.getDefault());
        } catch (final NoSuchMessageException e) {
            return key;
        }
    }

    public String get(final String key, final Object... args) {
        try {
            return messageSource.getMessage(key, args, Locale.getDefault());
        } catch (final NoSuchMessageException e) {
            return key;
        }
    }

}
