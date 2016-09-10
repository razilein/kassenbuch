package de.sg.computerinsel.tools.reparatur.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REPARATUR")
public class Reparatur extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "MITARBEITER_ID")
    private Mitarbeiter mitarbeiter;

    @Column(name = "nummer")
    private String nummer;

    @Column(name = "art")
    private Integer art;
    @Column(name = "geraet")
    private String geraet;

    @Column(name = "seriennummer")
    private String seriennummer;

    @Column(name = "symptome")
    private String symptome;

    @Column(name = "aufgaben")
    private String aufgaben;

    @Column(name = "geraetepasswort")
    private String geraetepasswort;

    @Column(name = "expressbeabeitung")
    private Boolean expressbeabeitung;

    @Column(name = "abholdatum")
    private Date abholdatum;

    @Column(name = "abholzeit")
    private Time abholzeit;

    @Column(name = "kostenvoranschlag")
    private String kostenvoranschlag;

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

}
