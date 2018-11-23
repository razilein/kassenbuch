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
@Table(name = "MITARBEITER")
@Getter
@Setter
public class Mitarbeiter extends IntegerBaseObject {

    @NotEmpty(message = "Bitte geben Sie den Nachnamen an.")
    @Size(max = 50, message = "Der Nachname darf nicht länger als 50 Zeichen sein.")
    @Column(name = "nachname")
    private String nachname;

    @NotEmpty(message = "Bitte geben Sie den Vornamen an.")
    @Size(max = 50, message = "Der Vorname darf nicht länger als 50 Zeichen sein.")
    @Column(name = "vorname")
    private String vorname;

    @Size(max = 50, message = "Die E-Mail-Adresse darf nicht länger als 50 Zeichen sein.")
    @Column(name = "email")
    private String email;

    @Size(max = 100, message = "Die private E-Mail-Adresse darf nicht länger als 50 Zeichen sein.")
    @Column(name = "email_privat")
    private String emailPrivat;

    @Size(max = 50, message = "Die Telefonnummer darf nicht länger als 50 Zeichen sein.")
    @Column(name = "telefon")
    private String telefon;

    public String getCompleteName() {
        return nachname + ", " + vorname;
    }

}
