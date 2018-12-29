package de.sg.computerinsel.tools.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PROTOKOLL")
@Getter
@Setter
@NoArgsConstructor
public class Protokoll extends IntegerBaseObject {

    @AllArgsConstructor
    @Getter
    public enum Protokolltyp {
        ANGESEHEN(0), ERSTELLT(1), GEAENDERT(2), GELOESCHT(3);

        private final int code;
    }

    @AllArgsConstructor
    @Getter
    public enum Protokolltabelle {
        REPARATUR("R"), KUNDE("K"), MITARBEITER("M"), FILIALE("F"), RECHTE("RE"), KATEGORIE("IK"), GRUPPE("IG"), PRODUKT("IP"), RECHNUNG(
                "I");

        private final String code;
    }

    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "tablename")
    private String tablename;

    @Column(name = "bemerkung")
    @Size(max = 1000)
    private String bemerkung;

    @Column(name = "datum")
    @NotNull
    private LocalDateTime datum = LocalDateTime.now();

    @Column(name = "mitarbeiter")
    @NotNull
    private String mitarbeiter;

    @Column(name = "typ")
    private int typ = 0;

    public Protokoll(final Integer tableId, final Protokolltabelle tablename, final String bemerkung, final String mitarbeiter,
            final Protokolltyp typ) {
        this.datum = LocalDateTime.now();
        this.tableId = tableId;
        this.tablename = tablename.getCode();
        this.bemerkung = bemerkung;
        this.mitarbeiter = mitarbeiter;
        this.typ = typ.getCode();
    }

    public Protokoll(final String bemerkung, final String mitarbeiter) {
        this.datum = LocalDateTime.now();
        this.bemerkung = bemerkung;
        this.mitarbeiter = mitarbeiter;
        this.typ = 1;
    }

}
