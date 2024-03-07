package com.example.summerproject.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CustomMessageSource {
    private final MessageSource messageSource;
    public String get(String code, Object... objects) {
        return messageSource.getMessage(code, objects, getLocal());
    }
    public Locale getLocal() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getDisplayLanguage().equalsIgnoreCase("English")) {
            locale = new Locale("en", "English");
        }
        return locale;
    }
}