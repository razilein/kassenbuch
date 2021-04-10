package de.sg.computerinsel.tools.kunde.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

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
        setEmail(StringUtils.abbreviate(dto.getEmail(), LENGTH_EMAIL));
        setErstelltAm(LocalDateTime.now());
        setNachname(StringUtils.abbreviate(dto.getNachname(), LENGTH_NACHNAME));
        setOrt(StringUtils.abbreviate(dto.getOrt(), LENGTH_ORT));
        setPlz(StringUtils.abbreviate(dto.getPlz(), LENGTH_PLZ));
        setStrasse(StringUtils.abbreviate(dto.getStrasse(), LENGTH_STRASSE));
        setTelefon(StringUtils.abbreviate(dto.getTelefon(), LENGTH_TELEFON));
        setVorname(StringUtils.abbreviate(dto.getVorname(), LENGTH_VORNAME));
    }

    @Size(max = 50, message = "kunde.telefon.bemerkung.error")
    private String telefonBemerkung;

}
