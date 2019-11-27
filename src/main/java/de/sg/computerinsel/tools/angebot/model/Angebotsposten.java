package de.sg.computerinsel.tools.angebot.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ANGEBOTSPOSTEN")
@Getter
@Setter
public class Angebotsposten extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "angebot_id", referencedColumnName = "id")
    private Angebot angebot;

    @Column(name = "position")
    @Min(value = 1, message = "rechnung.posten.position.error")
    private int position = 1;

    @Column(name = "menge")
    @Min(value = 1, message = "rechnung.posten.menge.error.min")
    @Max(value = 32767, message = "rechnung.posten.menge.error.max")
    private int menge = 1;

    @Column(name = "bezeichnung")
    @Size(max = 500, message = "rechnung.posten.bezeichnung.error")
    private String bezeichnung;

    /*
     * Bruttopreis
     */
    @Column(name = "preis")
    private BigDecimal preis = BigDecimal.ZERO;

}
