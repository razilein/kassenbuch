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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

    @Size(max = MAX_LENGTH_MITARBEITER, message = "Der Mitarbeitername darf nicht länger als 200 Zeichen sein.")
    @Column(name = "mitarbeiter")
    private String mitarbeiter;

    @ManyToOne
    @JoinColumn(name = "kunde_id", referencedColumnName = "id")
    private Kunde kunde;

    @Size(max = 20, message = "Die Auftragsnummer darf nicht länger als 20 Zeichen sein.")
    @Column(name = "nummer")
    private String nummer;

    @Column(name = "art")
    private Integer art;

    @Size(max = 500, message = "Der Gerätename darf nicht länger als 500 Zeichen sein.")
    @Column(name = "geraet")
    private String geraet;

    @Size(max = 500, message = "Die Seriennummer darf nicht länger als 500 Zeichen sein.")
    @Column(name = "seriennummer")
    private String seriennummer;

    @Size(max = 1000, message = "Der im Feld 'Symptome' eingetragene Text darf nicht länger als 1000 Zeichen sein.")
    @Column(name = "symptome")
    private String symptome;

    @Size(max = 1000, message = "Der im Feld 'Aufgaben' eingetragene Text darf nicht länger als 1000 Zeichen sein.")
    @Column(name = "aufgaben")
    private String aufgaben;

    @Size(max = 50, message = "Der im Feld 'Gerätepasswort' eingetragene Text darf nicht länger als 50 Zeichen sein.")
    @Column(name = "geraetepasswort")
    private String geraetepasswort;

    @Column(name = "funktionsfaehig")
    private boolean funktionsfaehig;

    @Column(name = "expressbearbeitung")
    private boolean expressbearbeitung = false;

    @Column(name = "abholdatum")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate abholdatum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Column(name = "abholzeit")
    private LocalTime abholzeit;

    @Size(max = 300, message = "Der im Feld 'Kostenvoranschlag' eingetragene Text darf nicht länger als 300 Zeichen sein.")
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
