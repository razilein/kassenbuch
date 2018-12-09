package de.sg.computerinsel.tools;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EINSTELLUNGEN")
@Getter
@Setter
public class Einstellungen extends IntegerBaseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "wert")
    private String wert;

}
