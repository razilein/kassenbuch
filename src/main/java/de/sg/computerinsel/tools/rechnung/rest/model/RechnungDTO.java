package de.sg.computerinsel.tools.rechnung.rest.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
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
        rechnung.setArt(0);
        rechnung.setNameDrucken(true);
        rechnung.setDatum(LocalDate.now());
        rechnung.setKunde(new Kunde());
        rechnung.setReparatur(new Reparatur());
    }

}
