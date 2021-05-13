package de.sg.computerinsel.tools.angebot.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.sg.computerinsel.tools.angebot.model.Angebotsposten;

class AngebotDtoTest {

    @Test
    void testGetPreisNetto() {
        final List<Angebotsposten> posten = new ArrayList<>();
        posten.add(createPosten(new BigDecimal("429.00")));
        posten.add(createPosten(new BigDecimal("9.00")));

        final AngebotDto dto = new AngebotDto(null, posten);
        final BigDecimal result = dto.getPreisNetto(new BigDecimal("119"));
        assertEquals(result, new BigDecimal("368.07"));
    }

    @Test
    void testGetPreisBrutto() {
        final AngebotDto dto = new AngebotDto(null, null);
        final BigDecimal result = dto.getPreisBrutto(new BigDecimal("119"), new BigDecimal("368.07"), BigDecimal.ZERO);
        assertEquals(result, new BigDecimal("438.00"));
    }

    private Angebotsposten createPosten(final BigDecimal preis) {
        final Angebotsposten posten = new Angebotsposten();
        posten.setPreis(preis);
        return posten;
    }

}
