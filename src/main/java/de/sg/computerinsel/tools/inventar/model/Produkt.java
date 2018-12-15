package de.sg.computerinsel.tools.inventar.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUKT")
@Getter
@Setter
public class Produkt extends IntegerBaseObject {

    @ManyToOne
    @JoinColumn(name = "gruppe_id", referencedColumnName = "id")
    private Gruppe gruppe;

    @Column(name = "bestand")
    private int bestand = 0;

    @Column(name = "bestand_unendlich")
    private boolean bestandUnendlich;

    @NotEmpty(message = "Bitte geben Sie die Bezeichnung des Produktes an")
    @Size(max = 500, message = "Die Bezeichnung des Produktes darf nicht länger als 500 Zeichen sein")
    @Column(name = "bezeichnung")
    private String bezeichnung;

    @Size(max = 100, message = "Die EAN darf nicht länger als 100 Zeichen sein")
    @Column(name = "ean")
    private String ean;

    @Size(max = 100, message = "Der Herstellername darf nicht länger als 100 Zeichen sein")
    @Column(name = "hersteller")
    private String hersteller;

    @Column(name = "preis_ek")
    private BigDecimal preisEk = BigDecimal.ZERO;

    @Column(name = "preis_vk")
    private BigDecimal preisVk = BigDecimal.ZERO;

}
