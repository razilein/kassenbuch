package de.sg.computerinsel.tools.rechnung.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RECHNUNG")
@Getter
@Setter
public class Rechnung extends BaseRechnung {

    @Column(name = "name_drucken_bei_firma")
    private boolean nameDruckenBeiFirma = true;

}
