package de.sg.computerinsel.tools.angebot.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private static final BigDecimal DIVISOR = new BigDecimal("100");

    private Angebot angebot;

    private List<Angebotsposten> angebotsposten;

    public AngebotDto(final BigDecimal mwst) {
        angebot = new Angebot();
        angebot.setMwst(mwst);
        angebot.setKunde(new Kunde());
        angebot.setRabatt(BigDecimal.ZERO);
        angebot.setRabattP(BigDecimal.ZERO);

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
            text.append(CurrencyUtils.format(preis));
            text.append(" EUR)");
            text.append(System.lineSeparator());
        });
        final BigDecimal mwst = DIVISOR.add(angebot.getMwst());
        final BigDecimal netto = getPreisNetto(mwst);
        text.append("Gesamt Netto: ");
        text.append(CurrencyUtils.format(netto));
        text.append(" EUR");
        text.append(System.lineSeparator());

        angebot.setRabatt(angebot.getRabatt() == null ? BigDecimal.ZERO : angebot.getRabatt());
        angebot.setRabattP(angebot.getRabattP() == null ? BigDecimal.ZERO : angebot.getRabattP());
        final BigDecimal rabattBetrag = getRabattBetrag(netto);
        if (angebot.getRabatt().compareTo(BigDecimal.ZERO) > 0) {
            text.append("Rabatt: ");
            text.append(CurrencyUtils.format(rabattBetrag));
            text.append(" EUR");
            text.append(System.lineSeparator());
        } else if (angebot.getRabattP().compareTo(BigDecimal.ZERO) > 0) {
            text.append("Rabatt ");
            text.append(angebot.getRabattP());
            text.append("%: ");
            text.append(CurrencyUtils.format(rabattBetrag));
            text.append(" EUR");
            text.append(System.lineSeparator());
        }

        text.append("Gesamt Brutto: ");
        text.append(CurrencyUtils.format(getPreisBrutto(mwst, netto, rabattBetrag)));
        text.append(" EUR");
        return text.toString();
    }

    private BigDecimal getRabattBetrag(final BigDecimal netto) {
        BigDecimal rabattBetrag;
        if (angebot.getRabatt().compareTo(BigDecimal.ZERO) > 0) {
            rabattBetrag = angebot.getRabatt();
        } else if (angebot.getRabattP().compareTo(BigDecimal.ZERO) > 0) {
            rabattBetrag = netto.multiply(angebot.getRabattP()).divide(DIVISOR, RoundingMode.HALF_UP);
        } else {
            rabattBetrag = BigDecimal.ZERO;
        }
        return rabattBetrag;
    }

    private BigDecimal getPreisBrutto(final BigDecimal mwst, final BigDecimal netto, final BigDecimal rabattBetrag) {
        return netto.subtract(rabattBetrag).multiply(mwst).divide(DIVISOR, RoundingMode.HALF_UP);
    }

    private BigDecimal getPreisNetto(final BigDecimal mwst) {
        return angebotsposten.stream().map(p -> p.getPreis().multiply(new BigDecimal(p.getMenge())))
                .map(p -> p.multiply(DIVISOR).divide(mwst, RoundingMode.HALF_UP)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getNummerAnzeige() {
        return angebot.getNummerAnzeige();
    }

    public BigDecimal getRabattBrutto() {
        if (angebot.getRabatt().compareTo(BigDecimal.ZERO) == 0 && angebot.getRabattP().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        final BigDecimal mwst = DIVISOR.add(angebot.getMwst());
        final BigDecimal netto = getPreisNetto(mwst);
        final BigDecimal brutto = netto.multiply(mwst).divide(DIVISOR, RoundingMode.HALF_UP);
        final BigDecimal rabattBetrag = getRabattBetrag(netto);
        final BigDecimal mwstBetrag = netto.subtract(rabattBetrag).multiply(angebot.getMwst()).divide(DIVISOR, RoundingMode.HALF_UP);
        return brutto.subtract(mwstBetrag).subtract(netto).add(rabattBetrag).setScale(2, RoundingMode.HALF_UP);
    }

}
