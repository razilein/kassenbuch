package de.sg.computerinsel.tools.kunde.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "KUNDE")
@Getter
@Setter
public class Kunde extends IntegerBaseObject {

    @Column(name = "nummer")
    private Integer nummer;

    @Column(name = "nachname")
    @Size(max = 100, message = "Der Nachname darf nicht länger als 100 Zeichen sein.")
    private String nachname;

    @Column(name = "vorname")
    @Size(max = 50, message = "Der Vorname darf nicht länger als 50 Zeichen sein.")
    private String vorname;

    @Column(name = "firmenname")
    @Size(max = 200, message = "Der Firmenname darf nicht länger als 200 Zeichen sein.")
    private String firmenname;

    @Column(name = "strasse")
    @Size(max = 100, message = "Die Straße darf nicht länger als 100 Zeichen sein.")
    private String strasse;

    @Column(name = "plz")
    @Size(max = 8, message = "Die PLZ darf nicht länger als 8 Zeichen sein.")
    private String plz;

    @Column(name = "ort")
    @Size(max = 50, message = "Der Ort darf nicht länger als 50 Zeichen sein.")
    private String ort;

    @Column(name = "telefon")
    @Size(max = 50, message = "Die Telefonnummer darf nicht länger als 50 Zeichen sein.")
    private String telefon;

    @Column(name = "email")
    @Size(max = 100, message = "Die E-Mail-Adresse darf nicht länger als 100 Zeichen sein.")
    private String email;

    @Column(name = "bemerkung")
    @Size(max = 4000, message = "Das Bemerkungsfeld darf nicht länger als 4000 Zeichen sein.")
    private String bemerkung;

    @Column(name = "dsgvo")
    private boolean dsgvo = false;

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;

    public String getNameKomplett() {
        final StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(firmenname)) {
            builder.append(StringUtils.defaultString(firmenname));
            builder.append(System.lineSeparator());
        }
        if (StringUtils.isNotBlank(nachname)) {
            builder.append(StringUtils.defaultString(nachname));
        }
        if (StringUtils.isNotBlank(vorname)) {
            builder.append(", ");
            builder.append(vorname);
        }
        return builder.toString();
    }

    public String getCompleteWithAdress() {
        final StringBuilder builder = new StringBuilder(getNameKomplett());
        builder.append(System.lineSeparator());
        if (StringUtils.isNotBlank(strasse)) {
            builder.append(strasse);
            builder.append(System.lineSeparator());
        }
        if (StringUtils.isNotBlank(plz) || StringUtils.isNotBlank(ort)) {
            builder.append(StringUtils.defaultString(plz));
            builder.append(StringUtils.SPACE);
            builder.append(StringUtils.defaultString(ort));
        }
        return builder.toString();
    }

    public String getCompleteWithAdressAndPhone() {
        final StringBuilder builder = new StringBuilder(getCompleteWithAdress());
        builder.append(System.lineSeparator());
        builder.append(StringUtils.defaultString(telefon));
        return builder.toString();
    }

}
