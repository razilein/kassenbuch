package de.sg.computerinsel.tools.einkauf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EINKAUF")
@Getter
@Setter
public class Einkauf extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "produkt_id", referencedColumnName = "id")
    private Produkt produkt;

    @Column(name = "menge")
    private int menge;

}
