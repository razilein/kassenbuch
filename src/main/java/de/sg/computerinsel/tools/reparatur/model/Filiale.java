package de.sg.computerinsel.tools.reparatur.model;

import java.math.BigDecimal;

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

    @NotEmpty(message = "einstellungen.filiale.name.error.empty")
    @Size(max = 50, message = "einstellungen.filiale.name.error.size")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "einstellungen.filiale.strasse.error.empty")
    @Size(max = 100, message = "einstellungen.filiale.strasse.error.size")
    @Column(name = "strasse")
    private String strasse;

    @NotEmpty(message = "einstellungen.filiale.plz.error.empty")
    @Size(max = 8, message = "einstellungen.filiale.plz.error.size")
    @Column(name = "plz")
    private String plz;

    @NotEmpty(message = "einstellungen.filiale.ort.error.empty")
    @Size(max = 50, message = "einstellungen.filiale.ort.error.size")
    @Column(name = "ort")
    private String ort;

    @NotEmpty(message = "einstellungen.filiale.telefon.error.empty")
    @Size(max = 50, message = "einstellungen.filiale.telefon.error.size")
    @Column(name = "telefon")
    private String telefon;

    @NotEmpty(message = "einstellungen.filiale.email.error.empty")
    @Size(max = 50, message = "einstellungen.filiale.email.error.size")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "einstellungen.filiale.kuerzel.error.empty")
    @Size(max = 3, message = "einstellungen.filiale.kuerzel.error.size")
    @Column(name = "kuerzel")
    private String kuerzel;

    @Size(max = 100, message = "einstellungen.filiale.rechnungTextAndereFiliale.error.size")
    @Column(name = "rechnung_text_andere_filiale")
    private String rechnungTextAndereFiliale;

    @Column(name = "zaehler_rechnung")
    private int zaehlerRechnung;

    @Column(name = "zaehler_reparaturauftrag")
    private int zaehlerReparaturauftrag;

    @Column(name = "zaehler_angebot")
    private int zaehlerAngebot;

    @Column(name = "ausgangsbetrag")
    private BigDecimal ausgangsbetrag;

}
