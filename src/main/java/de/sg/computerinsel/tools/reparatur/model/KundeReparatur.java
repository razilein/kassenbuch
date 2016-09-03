package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Sita Geßner
 */
@Entity
@Table(name = "KUNDE_REPARATUR")
public class KundeReparatur extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "KUNDE_ID")
    private Kunde kunde;

    @ManyToOne
    @JoinColumn(name = "REPARATUR_ID")
    private Reparatur reparatur;

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(final Kunde kunde) {
        this.kunde = kunde;
    }

    public Reparatur getReparatur() {
        return reparatur;
    }

    public void setReparatur(final Reparatur reparatur) {
        this.reparatur = reparatur;
    }

}
