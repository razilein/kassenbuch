package de.sg.computerinsel.tools.bestellung.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseBestellung extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "angebot_id", referencedColumnName = "id")
    private Angebot angebot;

    @NotEmpty(message = "bestellung.anzahlung.error.empty")
    @Size(max = 300, message = "bestellung.anzahlung.error.size")
    @Column(name = "anzahlung")
    private String anzahlung;

    @Size(max = 2000, message = "bestellung.beschreibung.error.size")
    @Column(name = "beschreibung")
    private String beschreibung;

    @Column(name = "datum")
    private LocalDate datum;

    @Column(name = "erledigt")
    private boolean erledigt = false;

    @Column(name = "erledigungsdatum")
    private LocalDateTime erledigungsdatum;

    @Column(name = "ersteller")
    private String ersteller;

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;

    @ManyToOne
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

    @ManyToOne
    @JoinColumn(name = "kunde_id", referencedColumnName = "id")
    private Kunde kunde;

    @NotEmpty(message = "bestellung.kosten.error.empty")
    @Size(max = 300, message = "bestellung.kosten.error.size")
    @Column(name = "kosten")
    private String kosten;

    @Column(name = "nummer")
    private int nummer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getDatum() {
        return datum;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public String getNummerAnzeige() {
        final String nummerAnzeige = String.valueOf(this.getNummer());
        return StringUtils.left(nummerAnzeige, 2) + "-B" + StringUtils.substring(nummerAnzeige, 2);
    }

}
