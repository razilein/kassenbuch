package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.Validate;

import de.sg.computerinsel.tools.rest.model.MitarbeiterDTO;
import de.sg.computerinsel.tools.rest.model.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "MITARBEITER")
@Getter
@Setter
@NoArgsConstructor
public class Mitarbeiter extends IntegerBaseObject {

    public static final int MAX_LENGTH_BENUTZERNAME = 50;

    public static final int MIN_LENGTH_BENUTZERNAME = 6;

    @ManyToOne
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

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

    @NotEmpty(message = "Bitte geben Sie einen Benutzernamen an.")
    @Size(min = MIN_LENGTH_BENUTZERNAME, max = MAX_LENGTH_BENUTZERNAME, message = "Der Benutzername darf nicht kürzer als 6 und nicht länger als 50 Zeichen sein.")
    @Column(name = "benutzername")
    private String benutzername;

    @NotEmpty(message = "Bitte geben Sie ein Passwort an.")
    @Size(min = 10, max = 500, message = "Das Passwort darf nicht kürzer als 10 und nicht länger als 500 Zeichen sein.")
    @Column(name = "passwort")
    private String passwort;

    public Mitarbeiter(final MitarbeiterDTO mitarbeiter) {
        Validate.notNull(mitarbeiter, "mitarbeiter darf nicht null sein.");
        this.nachname = mitarbeiter.getNachname();
        this.vorname = mitarbeiter.getVorname();
        this.email = mitarbeiter.getEmail();
        this.emailPrivat = mitarbeiter.getEmailPrivat();
        this.telefon = mitarbeiter.getTelefon();
    }

    public Mitarbeiter(final UserDTO anmeldedaten) {
        Validate.notNull(anmeldedaten, "anmeldedaten darf nicht null sein.");
        this.benutzername = anmeldedaten.getUsername();
        this.passwort = anmeldedaten.getPassword();
    }

    public String getCompleteName() {
        return nachname + ", " + vorname;
    }

    public String getCompleteNameReverse() {
        return vorname + " " + nachname;
    }

}
