package de.sg.computerinsel.tools.kunde.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseKunde extends IntegerBaseObject {

    protected static final int LENGTH_EMAIL = 100;

    protected static final int LENGTH_TELEFON = 50;

    protected static final int LENGTH_ORT = 50;

    protected static final int LENGTH_PLZ = 8;

    protected static final int LENGTH_STRASSE = 100;

    protected static final int LENGTH_VORNAME = 50;

    protected static final int LENGTH_NACHNAME = 100;

    @Column(name = "anrede")
    private Integer anrede;

    @Column(name = "akademischer_titel")
    @Size(max = 50, message = "kunde.akademischer_titel.error")
    private String akademischerTitel;

    @Column(name = "nummer")
    private Integer nummer;

    @Column(name = "nachname")
    @Size(max = LENGTH_NACHNAME, message = "kunde.nachname.error")
    private String nachname;

    @Column(name = "vorname")
    @Size(max = LENGTH_VORNAME, message = "kunde.vorname.error")
    private String vorname;

    @Column(name = "firmenname")
    @Size(max = 200, message = "kunde.firmenname.error")
    private String firmenname;

    @Column(name = "strasse")
    @Size(max = LENGTH_STRASSE, message = "kunde.strasse.error")
    private String strasse;

    @Column(name = "plz")
    @Size(max = LENGTH_PLZ, message = "kunde.plz.error")
    private String plz;

    @Column(name = "ort")
    @Size(max = LENGTH_ORT, message = "kunde.ort.error")
    private String ort;

    @Column(name = "telefon")
    @Size(max = LENGTH_TELEFON, message = "kunde.telefon.error")
    private String telefon;

    @Column(name = "mobiltelefon")
    @Size(max = 50, message = "kunde.mobiltelefon.error")
    private String mobiltelefon;

    @Column(name = "email")
    @Size(max = LENGTH_EMAIL, message = "kunde.email.error")
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

    @Column(name = "suchfeld_name")
    private String suchfeldName;

    private boolean problem;

    public String getNameKomplett() {
        final StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(firmenname)) {
            builder.append(StringUtils.defaultString(firmenname));
            builder.append(System.lineSeparator());
        }
        if (StringUtils.isNotBlank(akademischerTitel)) {
            builder.append(StringUtils.defaultString(akademischerTitel));
            builder.append(StringUtils.SPACE);
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
                if (StringUtils.isNotBlank(akademischerTitel)) {
                    builder.append(akademischerTitel);
                    builder.append(StringUtils.SPACE);
                }
                builder.append(nachname);
            }
        } else if (StringUtils.isBlank(nachname)) {
            builder.append(Anrede.FIRMA.getBriefAnrede());
        } else {
            builder.append(Anrede.ALLGEMEIN.getBriefAnrede());
            if (StringUtils.isNotBlank(akademischerTitel)) {
                builder.append(akademischerTitel);
                builder.append(StringUtils.SPACE);
            }
            if (StringUtils.isNotBlank(vorname)) {
                builder.append(vorname);
                builder.append(StringUtils.SPACE);
            }
            builder.append(nachname);
        }
        builder.append(",");
        return builder.toString();
    }

    public String getTelefonAnzeige() {
        return Arrays.asList(telefon, mobiltelefon).stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(" | "));
    }

}
