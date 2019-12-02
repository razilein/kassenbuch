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

}
