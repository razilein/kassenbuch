package de.sg.computerinsel.tools.angebot.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ANGEBOT")
@Getter
@Setter
public class Angebot extends BaseAngebot {

    @Column(name = "mwst")
    private BigDecimal mwst;

    @Column(name = "rabatt")
    private BigDecimal rabatt;

    @Column(name = "rabatt_p")
    private BigDecimal rabattP;

}
