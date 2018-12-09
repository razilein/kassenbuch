package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "ROLLE")
@Getter
@Setter
public class Rolle extends IntegerBaseObject {

    @NotEmpty(message = "Bitte geben Sie den Namen an.")
    @Size(max = 50, message = "Der Name darf nicht länger als 50 Zeichen sein.")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Bitte geben Sie die Beschreibung an.")
    @Size(max = 200, message = "Die Beschreibung darf nicht länger als 200 Zeichen sein.")
    @Column(name = "beschreibung")
    private String beschreibung;

}
