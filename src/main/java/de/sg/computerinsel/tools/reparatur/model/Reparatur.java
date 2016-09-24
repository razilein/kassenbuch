package de.sg.computerinsel.tools.reparatur.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "REPARATUR")
public class Reparatur extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "mitarbeiter_id")
    private Mitarbeiter mitarbeiter;

    @ManyToOne
    @JoinColumn(name = "kunde_id", referencedColumnName = "id")
    private Kunde kunde;

    @Size(max = 20, message = "Die Auftragsnummer darf nicht länger als 20 Zeichen sein.")
    @Column(name = "nummer")
    private String nummer;

    @Column(name = "art")
    private Integer art;

    @Size(max = 200, message = "Der Gerätename darf nicht länger als 200 Zeichen sein.")
    @Column(name = "geraet")
    private String geraet;

    @Size(max = 200, message = "Die Seriennummer darf nicht länger als 200 Zeichen sein.")
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

    @Column(name = "expressbeabeitung")
    private Boolean expressbeabeitung;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "abholdatum")
    private Date abholdatum;

    @Column(name = "abholzeit")
    private Time abholzeit;

    @Size(max = 10, message = "Der im Feld 'Kostenvoranschlag' eingetragene Text darf nicht länger als 10 Zeichen sein.")
    @Column(name = "kostenvoranschlag")
    private String kostenvoranschlag;

    @Column(name = "erledigt")
    private Boolean erledigt;

    @Column(name = "erledigungsdatum")
    private Date erledigungsdatum;

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(final Mitarbeiter mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(final String nummer) {
        this.nummer = nummer;
    }

    public Integer getArt() {
        return art;
    }

    public void setArt(final Integer art) {
        this.art = art;
    }

    public String getGeraet() {
        return geraet;
    }

    public void setGeraet(final String geraet) {
        this.geraet = geraet;
    }

    public String getSeriennummer() {
        return seriennummer;
    }

    public void setSeriennummer(final String seriennummer) {
        this.seriennummer = seriennummer;
    }

    public String getSymptome() {
        return symptome;
    }

    public void setSymptome(final String symptome) {
        this.symptome = symptome;
    }

    public String getAufgaben() {
        return aufgaben;
    }

    public void setAufgaben(final String aufgaben) {
        this.aufgaben = aufgaben;
    }

    public String getGeraetepasswort() {
        return geraetepasswort;
    }

    public void setGeraetepasswort(final String geraetepasswort) {
        this.geraetepasswort = geraetepasswort;
    }

    public Boolean getExpressbeabeitung() {
        return expressbeabeitung;
    }

    public void setExpressbeabeitung(final Boolean expressbeabeitung) {
        this.expressbeabeitung = expressbeabeitung;
    }

    public Date getAbholdatum() {
        return abholdatum;
    }

    public void setAbholdatum(final Date abholdatum) {
        this.abholdatum = abholdatum;
    }

    public Time getAbholzeit() {
        return abholzeit;
    }

    public void setAbholzeit(final Time abholzeit) {
        this.abholzeit = abholzeit;
    }

    public String getKostenvoranschlag() {
        return kostenvoranschlag;
    }

    public void setKostenvoranschlag(final String kostenvoranschlag) {
        this.kostenvoranschlag = kostenvoranschlag;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(final Kunde kunde) {
        this.kunde = kunde;
    }

    public Boolean getErledigt() {
        return erledigt;
    }

    public void setErledigt(final Boolean erledigt) {
        this.erledigt = erledigt;
    }

    public Date getErledigungsdatum() {
        return erledigungsdatum;
    }

    public void setErledigungsdatum(final Date erledigungsdatum) {
        this.erledigungsdatum = erledigungsdatum;
    }

    @Override
    public Object[] getTableModelObject() {
        return new Object[] { nummer, art, geraet, seriennummer, symptome, aufgaben, geraetepasswort, expressbeabeitung, abholdatum,
                abholzeit, kostenvoranschlag, mitarbeiter, getId(), getKunde() };
    }

    @Override
    public Object[] getTableModelObjectSearch() {
        return new Object[] { nummer, art, geraet, abholdatum, abholzeit, kunde, mitarbeiter, getId() };
    }

    @Override
    public String toString() {
        return "Reparatur " + nummer;
    }

}
