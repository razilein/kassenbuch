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

    @Column(name = "anrede")
    private Integer anrede;

    @Column(name = "nummer")
    private Integer nummer;

    @Column(name = "nachname")
    @Size(max = 100, message = "kunde.nachname.error")
    private String nachname;

    @Column(name = "vorname")
    @Size(max = 50, message = "kunde.vorname.error")
    private String vorname;

    @Column(name = "firmenname")
    @Size(max = 200, message = "kunde.firmenname.error")
    private String firmenname;

    @Column(name = "strasse")
    @Size(max = 100, message = "kunde.strasse.error")
    private String strasse;

    @Column(name = "plz")
    @Size(max = 8, message = "kunde.plz.error")
    private String plz;

    @Column(name = "ort")
    @Size(max = 50, message = "kunde.ort.error")
    private String ort;

    @Column(name = "telefon")
    @Size(max = 50, message = "kunde.telefon.error")
    private String telefon;

    @Column(name = "email")
    @Size(max = 100, message = "kunde.email.error")
    private String email;

    @Column(name = "bemerkung")
    @Size(max = 4000, message = "kunde.bemerkung.error")
    private String bemerkung;

    @Column(name = "dsgvo")
    private boolean dsgvo = false;

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;

    @Column(name = "name_drucken_bei_firma")
    private boolean nameDruckenBeiFirma = true;

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

    public String getBriefanrede() {
        final StringBuilder builder = new StringBuilder();
        if (anrede != null) {
            final Anrede a = Anrede.getByCode(anrede);
            builder.append(a.getBriefAnrede());
            if (a == Anrede.FRAU || a == Anrede.HERR) {
                builder.append(nachname);
            }
        } else if (StringUtils.isBlank(nachname)) {
            builder.append(Anrede.FIRMA.getBriefAnrede());
        } else {
            builder.append(Anrede.ALLGEMEIN.getBriefAnrede());
            if (StringUtils.isNotBlank(vorname)) {
                builder.append(vorname);
                builder.append(StringUtils.SPACE);
            }
            builder.append(nachname);
        }
        builder.append(",");
        return builder.toString();
    }

}
