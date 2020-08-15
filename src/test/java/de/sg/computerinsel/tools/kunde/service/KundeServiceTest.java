package de.sg.computerinsel.tools.kunde.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KundeServiceTest {

    private KundeService service;

    @BeforeEach
    public void setUp() {
        service = new KundeService(null, null, null, null);
    }

    @Test
    void testCreateSuchfeldName() throws Exception {
        final Method method = service.getClass().getDeclaredMethod("createSuchfeldName", String.class, String.class, String.class);
        method.setAccessible(true);
        assertEquals(StringUtils.EMPTY, method.invoke(service, (String) null, (String) null, (String) null));
        assertEquals("MustermannGmbH", method.invoke(service, "Mustermann GmbH", (String) null, (String) null));
        assertEquals("Max", method.invoke(service, (String) null, "Max", (String) null));
        assertEquals("Mustermann", method.invoke(service, (String) null, (String) null, "Mustermann"));
        assertEquals("MustermannGmbHMaxMustermann", method.invoke(service, "Mustermann GmbH", "Max", "Mustermann"));
        assertEquals("MaxMustermann", method.invoke(service, (String) null, "Max", "Mustermann"));
        assertEquals("abcdefghijk", method.invoke(service, "a b c d", " e f g ", "h i j k     "));
    }

    @Test
    void testCreateSuchfeldTelefon() throws Exception {
        final Method method = service.getClass().getDeclaredMethod("createSuchfeldTelefon", String.class);
        method.setAccessible(true);
        assertEquals(null, method.invoke(service, (String) null));
        assertEquals(null, method.invoke(service, StringUtils.EMPTY));
        assertEquals("01234567*890", method.invoke(service, "01-2  345/ 67*89\\0"));
    }

    @ParameterizedTest
    /* @formatter:off */
    @CsvSource({
        "0341 123-456-7, 1234567",
        "0341 409-660, 409660",
        "0341 309-660, 309660",
        "+41 41 462 66 66, +41414626666",
        "+7 954 213-41-47, +79542134147",
        "+7 912 836-48-71, 007/9-128364871",
        "0162 291-111-1, +49 162 2911-111",
        "0341 123-456-7, 03411234567",
        "03745 789-789, 03745789789",
        "0341 123-456-7, 0341-1234/567",
        "0174 120-078-9, 01741200789",
        "0174 120-078-9, +491741200789",
        "0174 120-078-9, 0174 12/007/8-9",
        "0174 120-078-9, 01741200789 (AB)",
        "01575538000, 01575538000",
        "0335 101-911-0, 03351019110",
        "+39 335 101 9110, +393351019110",
        ", ",
        "'', ''",
    })
    /* @formatter:on */
    void testFormatTelefonnummer(final String expected, final String number) throws Exception {
        final Method method = service.getClass().getDeclaredMethod("formatTelefonnummer", String.class);
        method.setAccessible(true);
        assertEquals(expected, method.invoke(service, number));
    }

}
