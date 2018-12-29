package de.sg.computerinsel.tools.rechnung.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RECHNUNGSPOSTEN")
@Getter
@Setter
public class Rechnungsposten extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "rechnung_id", referencedColumnName = "id")
    private Rechnung rechnung;

    @Column(name = "position")
    @Min(value = 1, message = "Die Position des Postens darf nicht kleiner als 1 sein")
    private int position = 1;

    @ManyToOne
    @JoinColumn(name = "produkt_id", referencedColumnName = "id")
    private Produkt produkt;

    @Column(name = "menge")
    @Min(value = 1, message = "Die Menge muss mindestens 1 betragen")
    @Max(value = 99, message = "Die Menge darf maximal 999 betragen")
    private int menge = 1;

    @Column(name = "bezeichnung")
    @Size(max = 500, message = "Die Bezeichnung des Postens darf maximal 500 Zeichen lang sein")
    private String bezeichnung;

    @Column(name = "seriennummer")
    @Size(max = 100, message = "Die Seriennummer darf maximal 100 Zeichen lang sein")
    private String seriennummer;

    @Column(name = "hinweis")
    @Size(max = 100, message = "Der Hinweis darf maximal 100 Zeichen lang sein")
    private String hinweis;

    @Column(name = "preis")
    private BigDecimal preis = BigDecimal.ZERO;

    @Column(name = "rabatt")
    private BigDecimal rabatt = BigDecimal.ZERO;

    public BigDecimal getGesamt() {
        return preis.multiply(new BigDecimal(menge)).subtract(rabatt);
    }

}
