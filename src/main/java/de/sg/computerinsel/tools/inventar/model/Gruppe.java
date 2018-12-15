package de.sg.computerinsel.tools.inventar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "GRUPPE")
@Getter
@Setter
public class Gruppe extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "kategorie_id", referencedColumnName = "id")
    private Kategorie kategorie;

    @NotEmpty(message = "Bitte geben Sie die Bezeichnung der Gruppe an.")
    @Size(max = 100, message = "Die Bezeichnung der Gruppe darf nicht länger als 100 Zeichen sein.")
    @Column(name = "bezeichnung")
    private String bezeichnung;

}
