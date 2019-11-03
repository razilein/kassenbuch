package de.sg.computerinsel.tools.rechnung.rest.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.sg.computerinsel.tools.auftrag.model.Auftrag;
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

    public RechnungDTO() {
        rechnung = new Rechnung();
        rechnung.setArt(-1);
        rechnung.setNameDrucken(false);
        rechnung.setDatum(LocalDate.now());
        rechnung.setAuftrag(new Auftrag());
        rechnung.setKunde(new Kunde());
        rechnung.setReparatur(new Reparatur());
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
        return posten.stream().map(Rechnungsposten::getGesamt).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @JsonIgnore
    public BigDecimal getNettobetrag() {
        return getRechnungsbetrag().divide(new BigDecimal("1.19"), 2, RoundingMode.HALF_UP);
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
