package de.sg.computerinsel.tools.rechnung.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RECHNUNG")
@Getter
@Setter
public class Rechnung extends BaseRechnung {

    @Column(name = "name_drucken_bei_firma")
    private boolean nameDruckenBeiFirma = true;

    @Column(name = "lieferdatum")
    private LocalDate lieferdatum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getLieferdatum() {
        return lieferdatum;
    }

}
