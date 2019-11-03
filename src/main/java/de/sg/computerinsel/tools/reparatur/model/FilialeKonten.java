package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FILIALE_KONTEN")
@Getter
@Setter
public class FilialeKonten extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "haben_bar")
    private Integer habenBar;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "haben_ec")
    private Integer habenEc;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "haben_paypal")
    private Integer habenPaypal;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "haben_ueberweisung")
    private Integer habenUeberweisung;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "soll_bar")
    private Integer sollBar;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "soll_ec")
    private Integer sollEc;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "soll_paypal")
    private Integer sollPaypal;

    @Max(value = 9999, message = "einstellungen.filiale.konto.error.size")
    @Min(value = 0, message = "einstellungen.filiale.konto.error.size")
    @Column(name = "soll_ueberweisung")
    private Integer sollUeberweisung;

}
