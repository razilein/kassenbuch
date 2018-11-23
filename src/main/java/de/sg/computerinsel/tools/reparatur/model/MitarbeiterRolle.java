package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MITARBEITER_ROLLE")
@Getter
@Setter
public class MitarbeiterRolle extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "mitarbeiter_id")
    private Mitarbeiter mitarbeiter;

    @ManyToOne
    @JoinColumn(name = "rolle_id")
    private Rolle rolle;

}
