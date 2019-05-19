package de.sg.computerinsel.tools;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;

import lombok.experimental.UtilityClass;

/**
 * @author Sita Geßner
 */
@UtilityClass
public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final DateTimeFormatter FILENAME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    public static String format(final LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String format(final Date date) {
        return format(convert(date));
    }

    public static String now() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    public static LocalDate parse(final String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public static LocalDate parse(final String date, final String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static Date parseDate(final String date) {
        return convert(parse(date));
    }

    public static String nowDatetime() {
        return LocalDateTime.now().format(FILENAME_FORMATTER) + RandomUtils.nextInt();
    }

    public static LocalDate convert(final Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date convert(final LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date getYear(final Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.YEAR);
    }

    public LocalDate plusWorkdays(final LocalDate date, final int days) {
        if (days < 1) {
            return date;
        }

        LocalDate result = date;
        int addedDays = 0;
        while (addedDays < days) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++addedDays;
            }
        }

        return result;
    }

}
