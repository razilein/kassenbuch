package de.sg.computerinsel.tools;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CurrencyUtilsTest {

    @Test
    void testFormat() {
        assertThat(CurrencyUtils.format(new BigDecimal("123456789.89")), equalTo("123.456.789,89"));
        assertThat(CurrencyUtils.format(new BigDecimal("123.2")), equalTo("123,20"));
    }

    @Test
    void testFormatOhneTrenner() {
        assertThat(CurrencyUtils.formatOhneTrenner(new BigDecimal("123456789.89")), equalTo("123456789,89"));
        assertThat(CurrencyUtils.formatOhneTrenner(new BigDecimal("123.2")), equalTo("123,20"));
    }

}
