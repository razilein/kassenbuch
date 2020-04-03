package de.sg.computerinsel.tools.bestellung.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BESTELLUNG")
@Getter
@Setter
public class Bestellung extends BaseBestellung {

}
