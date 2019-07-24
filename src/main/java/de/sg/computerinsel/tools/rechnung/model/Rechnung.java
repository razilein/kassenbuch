package de.sg.computerinsel.tools.rechnung.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RECHNUNG")
@Getter
@Setter
public class Rechnung extends BaseRechnung {

}
