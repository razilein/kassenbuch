package de.sg.computerinsel.tools.kassenbuch.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.kassenbuch.KassenbuchErstellenUtils;

/**
 * @author Sita Geßner
 */
public class Rechnung {

	private String rechnungsnummer;

	private Date rechnungsdatum;
	
	private BigDecimal rechnungsbetrag;

	public BigDecimal getRechnungsbetrag() {
		return rechnungsbetrag;
	}
	
	public String getRechnungsnummer() {
		return rechnungsnummer;
	}
	
	public void setRechnungsnummer(final String rechnungsnummer) {
		this.rechnungsnummer = rechnungsnummer;
	}
	
	public Date getRechnungsdatum() {
		return rechnungsdatum;
	}
	
	public void setRechnungsdatum(final Date rechnungsdatum) {
		this.rechnungsdatum = rechnungsdatum;
	}

	public void setRechnungsbetrag(final BigDecimal rechnungsbetrag) {
		this.rechnungsbetrag = rechnungsbetrag;
	}
	
	public Integer getRechnungsnummerAsInt() {
		return StringUtils.isNumeric(rechnungsnummer) ? Integer.valueOf(rechnungsnummer) : Ints.tryParse(StringUtils.substring(
				rechnungsnummer, 1));
	}

	@Override
	public String toString() {
		return "Rechnung [rechnungsnummer=" + rechnungsnummer + ", rechnungsdatum=" + rechnungsdatum + ", rechnungsbetrag="
				+ rechnungsbetrag + "]";
	}

	public String toCsvString(final BigDecimal gesamtBetrag) {
		final String formattedDate = rechnungsdatum == null ? StringUtils.EMPTY : KassenbuchErstellenUtils.DATE_FORMAT
		        .format(rechnungsdatum);
		final String verwendungszweck = StringUtils.isNumeric(rechnungsnummer)
				|| StringUtils.isNumeric(StringUtils.substring(rechnungsnummer, 1)) ? "Rechnung: " + rechnungsnummer : rechnungsnummer;
		final String formattedRechnungsbetrag = new DecimalFormat("#0.00").format(rechnungsbetrag);
		return Joiner.on(";").join(formattedDate, verwendungszweck,
				StringUtils.startsWith(formattedRechnungsbetrag, "-") ? "" : formattedRechnungsbetrag,
						!StringUtils.startsWith(formattedRechnungsbetrag, "-") ? "" : formattedRechnungsbetrag, gesamtBetrag, "\r\n");
	}
	
}
