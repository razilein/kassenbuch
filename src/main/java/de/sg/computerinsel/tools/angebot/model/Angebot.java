package de.sg.computerinsel.tools.angebot.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ANGEBOT")
@Getter
@Setter
public class Angebot extends BaseAngebot {

}
