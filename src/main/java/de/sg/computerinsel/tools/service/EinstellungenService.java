package de.sg.computerinsel.tools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.Einstellungen;
import de.sg.computerinsel.tools.dao.EinstellungenRepository;

/**
 * @author Sita Geßner
 */
@Service
public class EinstellungenService {

    @Autowired
    private EinstellungenRepository einstellungen;

    public Einstellungen getAusgangsbetrag() {
        return getEinstellung("kassenbuch.ausgangsbetrag");
    }

    public Einstellungen getLetzteCsvDateiPfad() {
        return getEinstellung("kassenbuch.letztecsvdateipfad");
    }

    public Einstellungen getLetztePdfDateiPfad() {
        return getEinstellung("kassenbuch.letztepdfdateipfad");
    }

    public Einstellungen getAblageverzeichnis() {
        return getEinstellung("kassenbuch.ablageverzeichnis");
    }

    public Einstellungen getRechnungsverzeichnis() {
        return getEinstellung("kassenbuch.rechnungsverzeichnis");
    }

    private Einstellungen getEinstellung(final String name) {
        return einstellungen.findByName(name).orElse(createEinstellung(name));
    }

    private Einstellungen createEinstellung(final String name) {
        final Einstellungen einstellung = new Einstellungen();
        einstellung.setName(name);
        return einstellung;
    }

    public void save(final Einstellungen einstellung) {
        einstellungen.save(einstellung);
    }

}
