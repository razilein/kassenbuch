package de.sg.computerinsel.tools.angebot.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name = "VANGEBOT")
@Getter
@Setter
public class VAngebot extends BaseAngebot {

    @Column(name = "gesamtbetrag")
    private BigDecimal gesamtbetrag;

    @Column(name = "gesamtbetrag_netto")
    private BigDecimal gesamtbetragNetto;

}
