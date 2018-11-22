package de.sg.computerinsel.tools.reparatur.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

/**
 * @author Sita Geßner
 */
@UtilityClass
public class FeiertagUtils {

    public static boolean isFeiertag(final LocalDate date) {
        final int jahr = date.getYear();

        final LocalDate ostersonntag = berechneOstersonntag(jahr);

        final List<LocalDate> feiertageSachsen = new ArrayList<>();
        feiertageSachsen.add(ostersonntag.plusDays(1));
        feiertageSachsen.add(ostersonntag.minusDays(2));
        feiertageSachsen.add(ostersonntag.plusDays(39));
        final LocalDate pfinstsonntag = ostersonntag.plusDays(49);
        feiertageSachsen.add(pfinstsonntag.plusDays(1));

        feiertageSachsen.add(LocalDate.of(jahr, Month.OCTOBER, 3));
        feiertageSachsen.add(LocalDate.of(jahr, Month.OCTOBER, 31));
        feiertageSachsen.add(berechneBussUndBetttag(jahr));
        feiertageSachsen.add(LocalDate.of(jahr, Month.DECEMBER, 24));
        feiertageSachsen.add(LocalDate.of(jahr, Month.DECEMBER, 25));
        feiertageSachsen.add(LocalDate.of(jahr, Month.DECEMBER, 26));
        feiertageSachsen.add(LocalDate.of(jahr, Month.DECEMBER, 31));
        feiertageSachsen.add(LocalDate.of(jahr, Month.JANUARY, 1));

        return feiertageSachsen.stream().anyMatch(f -> f.equals(date));
    }

    /**
     * https://www.java-forum.org/thema/feiertage-berechnen.14254/
     *
     * @param jahr
     * @return
     */
    private LocalDate berechneOstersonntag(final int jahr) {
        final int a = jahr % 19;
        final int b = jahr % 4;
        final int c = jahr % 7;
        int monat = 0;

        int m = (8 * (jahr / 100) + 13) / 25 - 2;
        final int s = jahr / 100 - jahr / 400 - 2;
        m = (15 + s - m) % 30;
        final int n = (6 + s) % 7;

        int d = (m + 19 * a) % 30;

        if (d == 29) {
            d = 28;
        } else if (d == 28 && a >= 11) {
            d = 27;
        }

        final int e = (2 * b + 4 * c + 6 * d + n) % 7;

        int tag = 21 + d + e + 1;

        if (tag > 31) {
            tag = tag % 31;
            monat = 3;
        }
        if (tag <= 31) {
            monat = 2;
        }

        return LocalDate.of(jahr, monat, tag);
    }

    /**
     * Mittwoch vor dem 23. November
     *
     * @param jahr
     * @return
     */
    private LocalDate berechneBussUndBetttag(final int jahr) {
        LocalDate bussUndBetttag = LocalDate.of(jahr, Month.NOVEMBER, 22);
        while (bussUndBetttag.getDayOfWeek() != DayOfWeek.WEDNESDAY) {
            bussUndBetttag = bussUndBetttag.minusDays(1);
        }
        return bussUndBetttag;
    }

}
