package de.sg.computerinsel.tools.rest.model;

import org.apache.commons.lang3.Validate;

import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MitarbeiterDTO {

    private Integer id;

    private String nachname;

    private String vorname;

    private String email;

    private String emailPrivat;

    private String telefon;

    public MitarbeiterDTO(final Mitarbeiter mitarbeiter) {
        Validate.notNull(mitarbeiter, "Mitarbeiter darf nicht null sein.");
        this.id = mitarbeiter.getId();
        this.nachname = mitarbeiter.getNachname();
        this.vorname = mitarbeiter.getVorname();
        this.email = mitarbeiter.getEmail();
        this.emailPrivat = mitarbeiter.getEmailPrivat();
        this.telefon = mitarbeiter.getTelefon();
    }

    public String getCompleteName() {
        return nachname + ", " + vorname;
    }

}
