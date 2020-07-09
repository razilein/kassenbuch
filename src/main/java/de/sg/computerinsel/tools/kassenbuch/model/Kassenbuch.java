package de.sg.computerinsel.tools.kassenbuch.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "KASSENBUCH")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Kassenbuch extends IntegerBaseObject {

    @Column(name = "ausgangsbetrag")
    @NotNull(message = "kassenbuch.ausgangsbetrag.error")
    private BigDecimal ausgangsbetrag;

    @Column(name = "datum")
    @NotNull(message = "kassenbuch.datum.error")
    private LocalDate datum;

    @Column(name = "ersteller")
    private String ersteller;

    @Column(name = "geloescht")
    private boolean geloescht;

    @Column(name = "datum_geloescht")
    private LocalDateTime datumGeloescht;

    @Column(name = "loescher")
    private String loescher;

    public Kassenbuch(final BigDecimal ausgangsbetrag, final LocalDate datum, final String ersteller) {
        this.ausgangsbetrag = ausgangsbetrag;
        this.datum = datum;
        this.ersteller = ersteller;
    }

}
