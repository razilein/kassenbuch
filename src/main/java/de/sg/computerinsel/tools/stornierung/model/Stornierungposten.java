package de.sg.computerinsel.tools.stornierung.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STORNIERUNGPOSTEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stornierungposten extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "stornierung_id", referencedColumnName = "id")
    private Stornierung stornierung;

    @ManyToOne
    @JoinColumn(name = "rechnungsposten_id", referencedColumnName = "id")
    private Rechnungsposten rechnungsposten;

}
