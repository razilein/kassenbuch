package de.sg.computerinsel.tools.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SearchQueryUtils {

    public static String getAndReplaceJoker(final Map<String, String> conditions, final String key) {
        return StringUtils.replace(conditions.get(key), "*", "%");
    }

}
