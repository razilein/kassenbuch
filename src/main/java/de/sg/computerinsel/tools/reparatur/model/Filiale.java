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
@Table(name = "FILIALE")
public class Filiale extends IntegerBaseObject {

    @NotNull(message = "Bitte geben Sie den Namen der Filiale an.")
    @Size(max = 50, message = "Der Name der Filiale darf nicht länger als 50 Zeichen sein.")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Bitte geben Sie den Straßennamen, sowie die Hausnummer der Filiale an.")
    @Size(max = 100, message = "Der Straßennamen darf nicht länger als 50 Zeichen sein.")
    @Column(name = "strasse")
    private String strasse;

    @NotNull(message = "Bitte geben Sie die PLZ der Filiale an.")
    @Size(max = 8, message = "Die PLZ darf nicht länger als 8 Zeichen sein.")
    @Column(name = "plz")
    private String plz;

    @NotNull(message = "Bitte geben Sie den Standort der Filiale an.")
    @Size(max = 50, message = "Der Ortsname darf nicht länger als 50 Zeichen sein.")
    @Column(name = "ort")
    private String ort;

    @NotNull(message = "Bitte geben Sie die Telefonnummer der Filiale an.")
    @Size(max = 50, message = "Die Telefonnummer darf nicht länger als 50 Zeichen sein.")
    @Column(name = "telefon")
    private String telefon;

    @NotNull(message = "Bitte geben Sie die E-Mail-Adresse der Filiale an.")
    @Size(max = 50, message = "Die E-Mail-Adresse darf nicht länger als 100 Zeichen sein.")
    @Column(name = "email")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

    public String toHtmlString() {
        return "<html>[" + name + "]<br>" + email + "<br>" + strasse + "<br> " + plz + " " + ort + "<br> " + telefon + "</html>";
    }
}
