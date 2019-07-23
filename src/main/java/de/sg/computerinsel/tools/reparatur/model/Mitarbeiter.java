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

    @NotEmpty(message = "einstellungen.mitarbeiter.nachname.error.empty")
    @Size(max = 50, message = "einstellungen.mitarbeiter.nachname.error.size")
    @Column(name = "nachname")
    private String nachname;

    @NotEmpty(message = "einstellungen.mitarbeiter.vorname.error.empty")
    @Size(max = 50, message = "einstellungen.mitarbeiter.vorname.error.size")
    @Column(name = "vorname")
    private String vorname;

    @Size(max = 50, message = "einstellungen.mitarbeiter.email.error.size")
    @Column(name = "email")
    private String email;

    @Size(max = 100, message = "einstellungen.mitarbeiter.email.privat.error.size")
    @Column(name = "email_privat")
    private String emailPrivat;

    @Size(max = 50, message = "einstellungen.mitarbeiter.telefon.error.size")
    @Column(name = "telefon")
    private String telefon;

    @NotEmpty(message = "einstellungen.mitarbeiter.benutzername.error.empty")
    @Size(min = MIN_LENGTH_BENUTZERNAME, max = MAX_LENGTH_BENUTZERNAME, message = "einstellungen.mitarbeiter.benutzername.error.size")
    @Column(name = "benutzername")
    private String benutzername;

    @NotEmpty(message = "einstellungen.mitarbeiter.psw.error.empty")
    @Size(min = 10, max = 500, message = "einstellungen.mitarbeiter.psw.error.size")
    @Column(name = "passwort")
    private String passwort;

    @Column(name = "druckansicht_neues_fenster")
    private boolean druckansichtNeuesFenster = true;

    @Column(name = "druckansicht_druckdialog")
    private boolean druckansichtDruckdialog = true;

    public Mitarbeiter(final MitarbeiterDTO mitarbeiter) {
        Validate.notNull(mitarbeiter, "mitarbeiter darf nicht null sein.");
        this.nachname = mitarbeiter.getNachname();
        this.vorname = mitarbeiter.getVorname();
        this.email = mitarbeiter.getEmail();
        this.emailPrivat = mitarbeiter.getEmailPrivat();
        this.telefon = mitarbeiter.getTelefon();
        this.druckansichtNeuesFenster = mitarbeiter.isDruckansichtNeuesFenster();
        this.druckansichtDruckdialog = mitarbeiter.isDruckansichtDruckdialog();
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
