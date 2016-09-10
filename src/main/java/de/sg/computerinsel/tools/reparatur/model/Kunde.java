package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "KUNDE")
public class Kunde extends IntegerBaseObject {

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "vorname")
    private String vorname;

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

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(final String strasse) {
        this.strasse = strasse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(final String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(final String ort) {
        this.ort = ort;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(final String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Kunde [nachname=" + nachname + ", vorname=" + vorname + ", strasse=" + strasse + ", plz=" + plz + ", ort=" + ort
                + ", telefon=" + telefon + ", email=" + email + "]";
    }

}
