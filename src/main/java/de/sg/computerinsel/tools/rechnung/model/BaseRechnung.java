package de.sg.computerinsel.tools.rechnung.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseRechnung extends IntegerBaseObject {

    public static final int MAX_LENGTH_MITARBEITER = 200;

    @ManyToOne
    @JoinColumn(name = "angebot_id", referencedColumnName = "id")
    private Angebot angebot;

    @ManyToOne
    @JoinColumn(name = "bestellung_id", referencedColumnName = "id")
    private Bestellung bestellung;

    @Column(name = "art")
    private int art = 0;

    @Column(name = "bezahlt")
    private boolean bezahlt = false;

    @Column(name = "datum")
    private LocalDate datum = LocalDate.now();

    @Column(name = "ersteller")
    @Size(max = MAX_LENGTH_MITARBEITER, message = "rechnung.ersteller.error")
    private String ersteller;

    @ManyToOne
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

    @ManyToOne
    @JoinColumn(name = "kunde_id", referencedColumnName = "id")
    private Kunde kunde;

    @Column(name = "name_drucken")
    private boolean nameDrucken = false;

    @Column(name = "nummer")
    private int nummer;

    @ManyToOne
    @JoinColumn(name = "reparatur_id", referencedColumnName = "id")
    private Reparatur reparatur;

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;

    @Column(name = "vorlage")
    private boolean vorlage;

    @Column(name = "zusatztext")
    @Size(max = 500, message = "rechnung.zusatztext.error")
    private String zusatztext;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDatum() {
        return datum;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public String getNummerAnzeige() {
        final String nummerAnzeige = String.valueOf(this.nummer);
        return StringUtils.left(nummerAnzeige, 2) + "-" + StringUtils.substring(nummerAnzeige, 2);
    }

    public String getNummerAnzeigeLieferschein() {
        final String nummerAnzeige = String.valueOf(this.nummer);
        return StringUtils.left(nummerAnzeige, 2) + "-L" + StringUtils.substring(nummerAnzeige, 2);
    }

}
