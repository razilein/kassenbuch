package de.sg.computerinsel.tools.rechnung.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name = "VRECHNUNG")
@Getter
@Setter
public class RechnungView extends BaseRechnung {

    @Column(name = "rechnungsbetrag")
    private BigDecimal rechnungsbetrag;

    @Column(name = "mit_angebot")
    private boolean mitAngebot;

    @Column(name = "mit_bestellung")
    private boolean mitBestellung;

    @Column(name = "mit_reparatur")
    private boolean mitReparatur;

}
