package de.sg.computerinsel.tools.kunde.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertEquals("%0%1%2%3%4%5%6%7%8%9%0%", method.invoke(service, "01-2  345/ 67*89\\0"));
    }

    @Test
    void testFormatTelefonnummer() throws Exception {
        final Method method = service.getClass().getDeclaredMethod("formatTelefonnummer", String.class);
        method.setAccessible(true);
        assertEquals("1234567", method.invoke(service, "1234567"));
        assertEquals("041 462 66 66", method.invoke(service, "+41414626666"));
        assertEquals("8 (954) 213-41-47", method.invoke(service, "+79542134147"));
        assertEquals("8 (912) 836-48-71", method.invoke(service, "007/9-128364871"));
        assertEquals("0162 2911111", method.invoke(service, "+49 162 2911-111"));
        assertEquals("0341 1234567", method.invoke(service, "03411234567"));
        assertEquals("03745 789789", method.invoke(service, "03745789789"));
        assertEquals("0341 1234567", method.invoke(service, "0341-1234/567"));
        assertEquals("0174 1200789", method.invoke(service, "01741200789"));
        assertEquals("0174 1200789", method.invoke(service, "0174 12/007/8-9"));
    }

}
