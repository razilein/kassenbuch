package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "MITARBEITER")
public class Mitarbeiter extends IntegerBaseObject {

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "vorname")
    private String vorname;

    public String getNachname() {
        return nachname;
    }

    public void setNachname(final String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }

}
