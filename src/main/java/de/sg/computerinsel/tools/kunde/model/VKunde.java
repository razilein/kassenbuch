package de.sg.computerinsel.tools.kunde.model;

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

    private int anzahlAuftraege;

    private int anzahlRechnungen;

    private int anzahlReparaturen;

}
