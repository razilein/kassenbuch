package de.sg.computerinsel.tools.rechnung.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RECHNUNG")
@Getter
@Setter
public class Rechnung extends IntegerBaseObject {

    public static final int MAX_LENGTH_MITARBEITER = 200;

    @Column(name = "art")
    private int art = 0;

    @Column(name = "bezahlt")
    private boolean bezahlt = false;

    @Column(name = "datum")
    private LocalDate datum = LocalDate.now();

    @Column(name = "ersteller")
    @Size(max = MAX_LENGTH_MITARBEITER, message = "Der Mitarbeitername darf nicht länger als 200 Zeichen sein")
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDatum() {
        return datum;
    }

}
