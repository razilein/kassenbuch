package de.sg.computerinsel.tools.inventar.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProduktDTO {

    private boolean bestandUnendlich;

    private String bezeichnung;

    private String ean;

    private BigDecimal preisEkBrutto = BigDecimal.ZERO;

    private BigDecimal preisVkBrutto = BigDecimal.ZERO;

    private BigDecimal preisEkNetto = BigDecimal.ZERO;

    private BigDecimal preisVkNetto = BigDecimal.ZERO;

    private String kategorieBezeichnung;

    private String gruppeBezeichnung;

    private String hersteller;

    public ProduktDTO(final Produkt produkt) {
        this.bestandUnendlich = produkt.isBestandUnendlich();
        this.bezeichnung = produkt.getBezeichnung();
        this.ean = produkt.getEan();
        this.preisEkBrutto = produkt.getPreisEkBrutto();
        this.preisVkBrutto = produkt.getPreisVkBrutto();
        this.preisEkNetto = produkt.getPreisEkNetto();
        this.preisVkNetto = produkt.getPreisVkNetto();
        this.gruppeBezeichnung = produkt.getGruppe().getBezeichnung();
        this.kategorieBezeichnung = produkt.getGruppe().getKategorie().getBezeichnung();
        this.hersteller = produkt.getHersteller();
    }

}
