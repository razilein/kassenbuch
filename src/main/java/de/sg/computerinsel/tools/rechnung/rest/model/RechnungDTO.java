package de.sg.computerinsel.tools.rechnung.rest.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RechnungDTO {

    private Rechnung rechnung;

    private List<Rechnungsposten> posten = new ArrayList<>();

    private boolean stornoBeachten;

    public RechnungDTO(final BigDecimal mwst) {
        rechnung = new Rechnung();
        rechnung.setMwst(mwst);
        rechnung.setArt(-1);
        rechnung.setNameDrucken(false);
        rechnung.setDatum(LocalDate.now());
        rechnung.setAngebot(new Angebot());
        rechnung.setBestellung(new Bestellung());
        rechnung.setKunde(new Kunde());
        rechnung.setReparatur(new Reparatur());
        rechnung.setRabatt(BigDecimal.ZERO);
        rechnung.setRabattP(BigDecimal.ZERO);
    }

    public Zahlart getArt() {
        return Zahlart.getByCode(rechnung.getArt());
    }

    public LocalDate getRechnungsjahr() {
        return LocalDate.of(rechnung.getDatum() == null ? 1990 : rechnung.getDatum().getYear(), Month.JANUARY, 1);
    }

    public Month getRechnungsmonat() {
        return rechnung.getDatum() == null ? Month.JANUARY : rechnung.getDatum().getMonth();
    }

    public BigDecimal getRechnungsbetrag() {
        BigDecimal betrag = posten.stream().map(Rechnungsposten::getGesamt).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (rechnung.getRabatt() != null) {
            betrag = betrag.subtract(rechnung.getRabatt());
        }
        if (rechnung.getRabattP() != null) {
            final BigDecimal rabatt = betrag.multiply(rechnung.getRabattP()).divide(new BigDecimal("100"));
            betrag = betrag.subtract(rabatt);
        }
        return betrag;
    }

    @JsonIgnore
    public BigDecimal getNettobetrag() {
        final BigDecimal mwst = new BigDecimal("100").add(rechnung.getMwst());
        return getRechnungsbetrag().multiply(new BigDecimal("100")).divide(mwst, 2, RoundingMode.HALF_UP);
    }

    @JsonIgnore
    public BigDecimal getUstBetrag() {
        return getRechnungsbetrag().subtract(getNettobetrag());
    }

    public String getKundenAdresse() {
        return rechnung == null || rechnung.getKunde() == null ? StringUtils.EMPTY
                : StringUtils.replace(rechnung.getKunde().getCompleteWithAdressAndPhone(), System.lineSeparator(), StringUtils.SPACE);
    }

}
