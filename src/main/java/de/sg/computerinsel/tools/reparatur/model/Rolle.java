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

    @NotEmpty(message = "rolle.name.error.empty")
    @Size(max = 50, message = "rolle.name.error.size")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "rolle.beschreibung.error.empty")
    @Size(max = 200, message = "rolle.beschreibung.error.size")
    @Column(name = "beschreibung")
    private String beschreibung;

}
