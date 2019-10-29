package de.sg.computerinsel.tools.auftrag.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name = "VAUFTRAEGE_JE_TAG")
@Getter
@Setter
public class VAuftraegeJeTag extends IntegerBaseObject {

    @Column(name = "datum")
    private LocalDate datum;

    @Column(name = "anzahl_reparatur")
    private int anzahlReparatur;

    @Column(name = "anzahl_auftrag")
    private int anzahlAuftrag;

    @Column(name = "anzahl_gesamt")
    private int anzahlGesamt;

}
