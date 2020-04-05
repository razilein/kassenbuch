package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name = "VREPARATUR")
@Getter
@Setter
public class VReparatur extends BaseReparatur {

    @Column(name = "bestellung_nr")
    private String bestellungNr;

    @Column(name = "kunde_nr")
    private String kundeNr;

    @Column(name = "reparatur_nr")
    private String reparaturNr;

}
