package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "MITARBEITER")
public class Mitarbeiter extends IntegerBaseObject {

    @NotNull(message = "Bitte geben Sie den Nachnamen an.")
    @Size(max = 50, message = "Der Nachname darf nicht länger als 50 Zeichen sein.")
    @Column(name = "nachname")
    private String nachname;

    @NotNull(message = "Bitte geben Sie den Vornamen an.")
    @Size(max = 50, message = "Der Vorname darf nicht länger als 50 Zeichen sein.")
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

    @Override
    public Object[] getTableModelObject() {
        return new Object[] { nachname, vorname, getId() };
    }

    @Override
    public String toString() {
        return "Mitarbeiter " + nachname + ", " + vorname;
    }

}
