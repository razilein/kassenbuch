package de.sg.computerinsel.tools.kassenbuch.rest.model;

import java.util.ArrayList;
import java.util.List;

import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuch;
import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuchposten;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KassenbuchDTO {

    private Kassenbuch kassenbuch = new Kassenbuch();

    private List<Kassenbuchposten> posten = new ArrayList<>();

}
