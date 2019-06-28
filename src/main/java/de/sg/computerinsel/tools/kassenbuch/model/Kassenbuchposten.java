package de.sg.computerinsel.tools.kassenbuch.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "KASSENBUCHPOSTEN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Kassenbuchposten extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "kassenbuch_id", referencedColumnName = "id")
    private Kassenbuch kassenbuch;

    @Column(name = "verwendungszweck")
    @Size(max = 200, message = "kassenbuch.posten.verwendungszweck.error")
    private String verwendungszweck;

    @Column(name = "betrag")
    @NotNull(message = "kassenbuch.posten.betrag.error")
    private BigDecimal betrag;

}
