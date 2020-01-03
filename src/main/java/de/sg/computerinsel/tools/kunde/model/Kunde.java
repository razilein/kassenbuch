package de.sg.computerinsel.tools.kunde.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sita Geßner
 */
@Entity
@Getter
@Setter
@Table(name = "KUNDE")
public class Kunde extends BaseKunde {

    @Size(max = 50, message = "kunde.telefon.bemerkung.error")
    private String telefonBemerkung;

}
