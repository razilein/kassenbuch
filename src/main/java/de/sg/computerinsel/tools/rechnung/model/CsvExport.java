package de.sg.computerinsel.tools.rechnung.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuchposten;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.util.CurrencyUtils;
import de.sg.computerinsel.tools.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CsvExport {

    public static final String WAEHRUNG_EUR = "EUR";

    private static final String DEFAULT_STEUERSCHLUESSEL = "0";

    private static final String EMPTY = "\"\"";

    private static final String STRING_VALUE = "\"%s\"";

    private static final String SEPARATOR = ";";

    public CsvExport(final RechnungDTO dto) {
        this.belegdatum = dto.getRechnung().getDatum();
        this.belegnummer = dto.getRechnung().getFiliale().getKuerzel() + dto.getRechnung().getNummerAnzeige();
        this.buchungstext = createBuchungstext(dto);
        this.buchungsbetrag = dto.getRechnungsbetrag();
        this.waehrung = WAEHRUNG_EUR;
    }

    public CsvExport(final Kassenbuchposten posten) {
        this.belegdatum = posten.getKassenbuch().getDatum();
        this.buchungstext = posten.getVerwendungszweck();
        this.buchungsbetrag = posten.getBetrag();
        this.waehrung = WAEHRUNG_EUR;
    }

    private String createBuchungstext(final RechnungDTO dto) {
        String text;
        switch (dto.getArt()) {
        case BAR:
            text = "Barverkauf ";
            break;
        case EC:
            text = "Verkauf mit EC ";
            break;
        case PAYPAL:
            text = "Verkauf per Paypal ";
            break;
        case UEBERWEISUNG:
            text = "Verkauf per Überweisung ";
            break;
        default:
            text = "Verkauf ";
            break;
        }
        text += dto.getRechnung().getFiliale().getKuerzel() + dto.getRechnung().getNummerAnzeige();
        return text;
    }

    private LocalDate belegdatum;

    private LocalDate buchungsdatum;

    private String belegnummernkreis;

    private String belegnummer;

    private String buchungstext;

    private BigDecimal buchungsbetrag;

    private Integer sollkonto;

    private Integer habenkonto;

    private String steuerschluessel;

    private String kostenstelle1;

    private String kostenstelle2;

    private String waehrung;

    public String toCsvString() {
        final List<String> csv = new ArrayList<>();
        csv.add(belegdatum == null ? StringUtils.EMPTY : DateUtils.format(belegdatum));
        csv.add(buchungsdatum == null ? StringUtils.EMPTY : DateUtils.format(buchungsdatum));
        csv.add(StringUtils.isBlank(belegnummernkreis) ? EMPTY : String.format(STRING_VALUE, belegnummernkreis));
        csv.add(StringUtils.isBlank(belegnummer) ? EMPTY : String.format(STRING_VALUE, belegnummer));
        csv.add(StringUtils.isBlank(buchungstext) ? EMPTY : String.format(STRING_VALUE, buchungstext));
        csv.add(CurrencyUtils.format(buchungsbetrag == null ? BigDecimal.ZERO : buchungsbetrag));
        csv.add(sollkonto == null ? StringUtils.EMPTY : String.valueOf(sollkonto));
        csv.add(habenkonto == null ? StringUtils.EMPTY : String.valueOf(habenkonto));
        csv.add(steuerschluessel == null ? DEFAULT_STEUERSCHLUESSEL : String.valueOf(steuerschluessel));
        csv.add(StringUtils.isBlank(kostenstelle1) ? EMPTY : String.valueOf(steuerschluessel));
        csv.add(StringUtils.isBlank(kostenstelle2) ? EMPTY : String.valueOf(steuerschluessel));
        csv.add(CurrencyUtils.format(buchungsbetrag == null ? BigDecimal.ZERO : buchungsbetrag));
        csv.add(StringUtils.isBlank(waehrung) ? EMPTY : String.format(STRING_VALUE, waehrung));
        return csv.stream().collect(Collectors.joining(SEPARATOR));
    }

}
