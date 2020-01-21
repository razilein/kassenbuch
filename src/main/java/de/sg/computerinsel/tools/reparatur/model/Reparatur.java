package de.sg.computerinsel.tools.reparatur.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "REPARATUR")
@Getter
@Setter
public class Reparatur extends IntegerBaseObject {

    public static final int MAX_LENGTH_MITARBEITER = 200;

    @ManyToOne
    @JoinColumn(name = "bestellung_id", referencedColumnName = "id")
    private Bestellung bestellung;

    @ManyToOne
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

    @Size(max = MAX_LENGTH_MITARBEITER, message = "reparatur.mitarbeiter.error.size")
    @NotEmpty(message = "reparatur.mitarbeiter.error.empty")
    @Column(name = "mitarbeiter")
    private String mitarbeiter;

    @ManyToOne
    @JoinColumn(name = "kunde_id", referencedColumnName = "id")
    private Kunde kunde;

    @Size(max = 20, message = "reparatur.nummer.error.size")
    @Column(name = "nummer")
    private String nummer;

    @Column(name = "art")
    private Integer art;

    @Size(max = 500, message = "reparatur.geraet.error.size")
    @Column(name = "geraet")
    private String geraet;

    @Size(max = 500, message = "reparatur.seriennummer.error.size")
    @Column(name = "seriennummer")
    private String seriennummer;

    @Size(max = 1000, message = "reparatur.symptome.error.size")
    @Column(name = "symptome")
    private String symptome;

    @Size(max = 1000, message = "reparatur.aufgaben.error.size")
    @Column(name = "aufgaben")
    private String aufgaben;

    @Size(max = 50, message = "reparatur.geraetepsw.error.size")
    @Column(name = "geraetepasswort")
    private String geraetepasswort;

    @Column(name = "geraetepasswort_art")
    private int geraetepasswortArt;

    @Column(name = "funktionsfaehig")
    private int funktionsfaehig;

    @Column(name = "expressbearbeitung")
    private boolean expressbearbeitung = false;

    @Column(name = "abholdatum")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate abholdatum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(name = "abholzeit")
    private LocalTime abholzeit;

    @NotEmpty(message = "reparatur.kostenvoranschlag.error.empty")
    @Size(max = 300, message = "reparatur.kostenvoranschlag.error.size")
    @Column(name = "kostenvoranschlag")
    private String kostenvoranschlag;

    @Column(name = "erledigt")
    private boolean erledigt = false;

    @Column(name = "erledigungsdatum")
    private LocalDateTime erledigungsdatum;

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;

    @Column(name = "bemerkung")
    private String bemerkung;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getAbholdatum() {
        return abholdatum;
    }

    public String getWochentagAbholdatum() {
        return getAbholdatum() == null ? null : getAbholdatum().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY);
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    public LocalTime getAbholzeit() {
        return abholzeit;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public String getWochentagErstelltAm() {
        return getErstelltAm() == null ? null : getErstelltAm().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY);
    }

}
