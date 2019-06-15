package de.sg.computerinsel.tools.inventar.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;

import de.sg.computerinsel.tools.CurrencyUtils;
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

    @Column(name = "aenderungsdatum")
    private LocalDateTime aenderungsdatum;

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

    @Column(name = "preis_ek_brutto")
    private BigDecimal preisEkBrutto = BigDecimal.ZERO;

    @Column(name = "preis_vk_brutto")
    private BigDecimal preisVkBrutto = BigDecimal.ZERO;

    @Column(name = "preis_ek_netto")
    private BigDecimal preisEkNetto = BigDecimal.ZERO;

    @Column(name = "preis_vk_netto")
    private BigDecimal preisVkNetto = BigDecimal.ZERO;

    @Formula("(SELECT COUNT(*) FROM rechnungsposten WHERE rechnungsposten.produkt_id = id)")
    private int anzahlVerkaeufe;

    public String getPreise() {
        final StringBuilder builder = new StringBuilder();
        appendPreis(builder, "EK Nto: ", preisEkNetto);
        appendPreis(builder, "VK Bto: ", preisVkBrutto);
        return builder.toString();
    }

    private void appendPreis(final StringBuilder builder, final String text, final BigDecimal preis) {
        if (preis != null) {
            builder.append(text);
            builder.append(CurrencyUtils.format(preis));
            builder.append(System.lineSeparator());
        }
    }

}
