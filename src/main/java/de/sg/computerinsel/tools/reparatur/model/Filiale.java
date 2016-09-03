package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "FILIALE")
public class Filiale extends IntegerBaseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "strasse")
    private String strasse;

    @Column(name = "plz")
    private String plz;

    @Column(name = "ort")
    private String ort;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "email")
    private String email;

    @Override
    public String toString() {
        return "Filiale [name=" + name + ", strasse=" + strasse + ", plz=" + plz + ", ort=" + ort + ", telefon=" + telefon + ", email="
                + email + "]";
    }

}
