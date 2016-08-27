package de.sg.computerinsel.tools.kassenbuch.model;

import javax.swing.JTextField;

/**
 * @author Sita Geßner
 */
public class Einstellungen {

    private final JTextField rechnungsverzeichnis = new JTextField(50);

    private final JTextField ablageverzeichnis = new JTextField(50);

    private final JTextField ausgangsbetrag = new JTextField(35);
    
    private final JTextField dateipfad = new JTextField(50);

    public JTextField getRechnungsverzeichnis() {
        return rechnungsverzeichnis;
    }

    public String getRechnungsverzeichnisText() {
        return rechnungsverzeichnis.getText();
    }

    public void setRechnungsverzeichnisText(final String rechnungsverzeichnis) {
        this.rechnungsverzeichnis.setText(rechnungsverzeichnis);
    }

    public JTextField getAblageverzeichnis() {
        return ablageverzeichnis;
    }

    public String getAblageverzeichnisText() {
        return ablageverzeichnis.getText();
    }

    public void setAblageverzeichnisText(final String ablageverzeichnis) {
        this.ablageverzeichnis.setText(ablageverzeichnis);
    }
    
    public JTextField getAusgangsbetrag() {
        return ausgangsbetrag;
    }
    
    public String getAusgangsbetragText() {
        return ausgangsbetrag.getText();
    }
    
    public void setAusgangsbetragText(final String ausgangsbetrag) {
        this.ausgangsbetrag.setText(ausgangsbetrag);
    }
    
    public JTextField getDateipfad() {
        return dateipfad;
    }

    public String getDateipfadText() {
        return dateipfad.getText();
    }
    
    public void setDateipfadText(final String dateipfad) {
        this.dateipfad.setText(dateipfad);
    }
}
