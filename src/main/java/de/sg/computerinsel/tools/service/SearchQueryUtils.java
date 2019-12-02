package de.sg.computerinsel.tools.service;

import java.util.Map;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SearchQueryUtils {

    public static String getAndReplaceOrAddJoker(final Map<String, String> conditions, final String key) {
        String cond = RegExUtils.replaceAll(conditions.get(key), "\\*", "%");
        if (StringUtils.isNotBlank(cond)) {
            cond = "%" + cond + "%";
        }
        return cond;
    }

    public static String getAndRemoveJoker(final Map<String, String> conditions, final String key) {
        return RegExUtils.replaceAll(conditions.get(key), "\\*", StringUtils.EMPTY);
    }

}
