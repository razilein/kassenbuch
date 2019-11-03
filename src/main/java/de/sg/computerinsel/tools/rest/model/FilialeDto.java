package de.sg.computerinsel.tools.rest.model;

import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.FilialeKonten;
import lombok.Data;

@Data
public class FilialeDto {

    private Filiale filiale = new Filiale();

    private FilialeKonten filialeKonten = new FilialeKonten();

}
