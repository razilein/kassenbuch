package de.sg.computerinsel.tools.kunde.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Table(name = "VKUNDE")
@Getter
@Setter
public class VKunde extends BaseKunde {

    private int anzahlAngebote;

    private int anzahlBestellungen;

    private int anzahlRechnungen;

    private int anzahlReparaturen;

    private int anzahlVorlagen;

    private String suchfeldTelefon;

    @Column(name = "suchfeld2_telefon")
    private String suchfeld2Telefon;

    private String suchfeldStrasse;

}
