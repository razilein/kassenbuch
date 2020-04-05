package de.sg.computerinsel.tools.reparatur.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "REPARATUR")
@Getter
@Setter
public class Reparatur extends BaseReparatur {

}
