package de.sg.computerinsel.tools.kunde.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.reparatur.service.ReparaturCsvDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sita Geßner
 */
@Entity
@Getter
@Setter
@Table(name = "KUNDE")
@NoArgsConstructor
public class Kunde extends BaseKunde {

    public Kunde(final ReparaturCsvDto dto) {
        setEmail(dto.getEmail());
        setErstelltAm(LocalDateTime.now());
        setNachname(dto.getNachname());
        setOrt(dto.getOrt());
        setPlz(dto.getPlz());
        setStrasse(dto.getStrasse());
        setTelefon(dto.getTelefon());
        setVorname(dto.getVorname());
    }

    @Size(max = 50, message = "kunde.telefon.bemerkung.error")
    private String telefonBemerkung;

}
