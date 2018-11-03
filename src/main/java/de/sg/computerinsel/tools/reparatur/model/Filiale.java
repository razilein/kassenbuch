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
@Table(name = "FILIALE")
@Getter
@Setter
public class Filiale extends IntegerBaseObject {

    @NotEmpty(message = "Bitte geben Sie den Namen der Filiale an.")
    @Size(max = 50, message = "Der Name der Filiale darf nicht länger als 50 Zeichen sein.")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Bitte geben Sie den Straßennamen, sowie die Hausnummer der Filiale an.")
    @Size(max = 100, message = "Der Straßennamen darf nicht länger als 50 Zeichen sein.")
    @Column(name = "strasse")
    private String strasse;

    @NotEmpty(message = "Bitte geben Sie die PLZ der Filiale an.")
    @Size(max = 8, message = "Die PLZ darf nicht länger als 8 Zeichen sein.")
    @Column(name = "plz")
    private String plz;

    @NotEmpty(message = "Bitte geben Sie den Standort der Filiale an.")
    @Size(max = 50, message = "Der Ortsname darf nicht länger als 50 Zeichen sein.")
    @Column(name = "ort")
    private String ort;

    @NotEmpty(message = "Bitte geben Sie die Telefonnummer der Filiale an.")
    @Size(max = 50, message = "Die Telefonnummer darf nicht länger als 50 Zeichen sein.")
    @Column(name = "telefon")
    private String telefon;

    @NotEmpty(message = "Bitte geben Sie die E-Mail-Adresse der Filiale an.")
    @Size(max = 50, message = "Die E-Mail-Adresse darf nicht länger als 100 Zeichen sein.")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Bitte geben Sie das Kürzel der Filiale an. Dieses wird den Auftragsnummer vorangestellt.")
    @Size(max = 3, message = "Das Kürzel darf nicht länger als 3 Zeichen sein.")
    @Column(name = "kuerzel")
    private String kuerzel;

    public String toHtmlString() {
        return "<html>[" + name + " (" + kuerzel + ")" + "]<br>" + email + "<br>" + strasse + "<br> " + plz + " " + ort + "<br> " + telefon
                + "</html>";
    }

    @Override
    public Object[] getTableModelObject() {
        return new Object[] { name, kuerzel, email, strasse, plz, ort, telefon, getId() };
    }

    @Override
    public String toString() {
        return "Filiale " + name;
    }
}
