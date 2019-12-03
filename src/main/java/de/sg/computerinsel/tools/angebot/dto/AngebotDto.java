package de.sg.computerinsel.tools.angebot.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.sg.computerinsel.tools.CurrencyUtils;
import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.angebot.model.Angebotsposten;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AngebotDto {

    private Angebot angebot;

    private List<Angebotsposten> angebotsposten;

    public AngebotDto() {
        angebot = new Angebot();
        angebot.setKunde(new Kunde());

        angebotsposten = new ArrayList<>();
    }

    public String getText() {
        final StringBuilder text = new StringBuilder();
        angebotsposten.stream().forEach(p -> {
            text.append(p.getMenge());
            text.append(" x ");
            text.append(p.getBezeichnung());
            text.append(" (");
            final BigDecimal preis = p.getPreis().multiply(new BigDecimal(p.getMenge()));
            text.append(CurrencyUtils.format(preis.multiply(new BigDecimal("1.19"))));
            text.append(" EUR)");
            text.append(System.lineSeparator());
        });
        text.append(System.lineSeparator());
        final BigDecimal netto = angebotsposten.stream().map(p -> p.getPreis().multiply(new BigDecimal(p.getMenge())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        text.append("Gesamt Brutto: ");
        text.append(CurrencyUtils.format(netto.multiply(new BigDecimal("1.19"))));
        text.append(" EUR");
        return text.toString();
    }

}