package de.sg.computerinsel.tools.rechnung.rest.model;

import java.time.LocalDate;
import java.util.List;

import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.stornierung.model.Stornierung;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StornoDto extends RechnungDTO {

    public StornoDto(final Rechnung rechnung, final List<Rechnungsposten> posten) {
        super(rechnung.getMwst());
        setRechnung(rechnung);
        setPosten(posten);
        storno = new Stornierung();
        storno.setRechnung(rechnung);
        storno.setKunde(rechnung.getKunde());
        storno.setDatum(LocalDate.now());
        storno.setFiliale(rechnung.getFiliale());
        storno.setVollstorno(true);
    }

    public StornoDto(final Stornierung storno, final List<Rechnungsposten> posten) {
        super(storno.getRechnung().getMwst());
        this.storno = storno;
        setRechnung(storno.getRechnung());
        setPosten(posten);
    }

    public StornoDto() {
        super(null);
    }

    private Stornierung storno;

}
