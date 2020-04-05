package de.sg.computerinsel.tools.bestellung.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name = "VBESTELLUNG")
@Getter
@Setter
public class VBestellung extends BaseBestellung {

    @Column(name = "angebot_nr")
    private String angebotNr;

    @Column(name = "bestellung_nr")
    private String bestellungNr;

    @Column(name = "kunde_nr")
    private String kundeNr;

}
