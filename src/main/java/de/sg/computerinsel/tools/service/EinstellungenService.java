package de.sg.computerinsel.tools.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.Einstellungen;
import de.sg.computerinsel.tools.dao.EinstellungenRepository;
import de.sg.computerinsel.tools.kassenbuch.rest.model.Kassenstand;

/**
 * @author Sita Geßner
 */
@Service
public class EinstellungenService {

    private static final List<String> KASSENBESTAND_SETTING_KEYS = ImmutableList.of("500", "200", "100", "50", "20", "10", "5", "2", "1",
            "050", "020", "010", "005", "002", "001");

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

    public List<Kassenstand> getKassenstand() {
        final List<Kassenstand> kassenstaende = new ArrayList<>();
        for (final String key : KASSENBESTAND_SETTING_KEYS) {
            final Einstellungen einstellung = getEinstellung("kassenbuch.kassenstand." + key);
            final int anzahl = StringUtils.isNumeric(einstellung.getWert()) ? Ints.tryParse(einstellung.getWert()) : 0;
            kassenstaende.add(new Kassenstand(anzahl, key));
        }
        return kassenstaende;
    }

    public void saveKassenstand(final List<Kassenstand> kassenstaende) {
        for (final Kassenstand kassenstand : kassenstaende) {
            final Einstellungen einstellung = getEinstellung("kassenbuch.kassenstand." + kassenstand.getKey());
            einstellung.setWert(String.valueOf(kassenstand.getAnzahl()));
            einstellungen.save(einstellung);
        }
    }

}
