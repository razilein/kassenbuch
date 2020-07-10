package de.sg.computerinsel.tools.stornierung.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.rechnung.model.BaseRechnung;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "STORNIERUNG")
@Getter
@Setter
public class Stornierung extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "rechnung_id", referencedColumnName = "id")
    private Rechnung rechnung;

    @ManyToOne
    @JoinColumn(name = "kunde_id", referencedColumnName = "id")
    private Kunde kunde;

    @Column(name = "datum")
    private LocalDate datum;

    @Column(name = "ersteller")
    @Size(max = BaseRechnung.MAX_LENGTH_MITARBEITER, message = "rechnung.ersteller.error")
    private String ersteller;

    @ManyToOne
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

    @Column(name = "grund")
    @Size(max = 1000, message = "storno.grund.error")
    private String grund;

    @Column(name = "nummer")
    private int nummer;

    @Column(name = "vollstorno")
    private boolean vollstorno;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDatum() {
        return datum;
    }

    public String getNummerAnzeige() {
        final String nummerAnzeige = String.valueOf(nummer);
        return filiale.getKuerzel() + StringUtils.left(nummerAnzeige, 2) + "-" + StringUtils.substring(nummerAnzeige, 2);
    }

}
