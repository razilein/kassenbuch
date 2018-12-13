package de.sg.computerinsel.tools.inventar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "KATEGORIE")
@Getter
@Setter
public class Kategorie extends IntegerBaseObject {

    @NotEmpty(message = "Bitte geben Sie die Bezeichnung der Kategorie an.")
    @Size(max = 100, message = "Die Bezeichnung der Kategorie darf nicht länger als 100 Zeichen sein.")
    @Column(name = "bezeichnung")
    private String bezeichnung;

}
