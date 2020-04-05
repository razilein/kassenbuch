package de.sg.computerinsel.tools.angebot.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.rechnung.model.BaseRechnung;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseAngebot extends IntegerBaseObject {

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;

    @Column(name = "ersteller")
    @Size(max = BaseRechnung.MAX_LENGTH_MITARBEITER, message = "rechnung.ersteller.error")
    private String ersteller;

    @Column(name = "erledigt")
    private boolean erledigt = false;

    @Column(name = "erledigungsdatum")
    private LocalDateTime erledigungsdatum;

    @ManyToOne
    @JoinColumn(name = "filiale_id", referencedColumnName = "id")
    private Filiale filiale;

    @ManyToOne
    @JoinColumn(name = "kunde_id", referencedColumnName = "id")
    private Kunde kunde;

    @Column(name = "nummer")
    private int nummer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public String getNummerAnzeige() {
        final String nummerAnzeige = String.valueOf(getNummer());
        return StringUtils.left(nummerAnzeige, 2) + "-A" + StringUtils.substring(nummerAnzeige, 2);
    }

}
