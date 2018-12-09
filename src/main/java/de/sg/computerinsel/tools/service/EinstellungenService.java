package de.sg.computerinsel.tools.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.Einstellungen;
import de.sg.computerinsel.tools.dao.EinstellungenRepository;
import de.sg.computerinsel.tools.dao.FilialeRepository;
import de.sg.computerinsel.tools.dao.MitarbeiterRepository;
import de.sg.computerinsel.tools.dao.RolleRepository;
import de.sg.computerinsel.tools.kassenbuch.rest.model.Kassenstand;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.Rolle;
import de.sg.computerinsel.tools.rest.model.MitarbeiterDTO;
import lombok.AllArgsConstructor;

/**
 * @author Sita Geßner
 */
@Service
@AllArgsConstructor
public class EinstellungenService {

    private static final String DSGVO_FILENAME = "Einwilligung_DSGVO.pdf";

    private static final String SALT_FILENAME = "salt.txt";

    private static final List<String> KASSENBESTAND_SETTING_KEYS = ImmutableList.of("500", "200", "100", "50", "20", "10", "5", "2", "1",
            "050", "020", "010", "005", "002", "001");

    private final EinstellungenRepository einstellungen;

    private final FilialeRepository filialeRepository;

    private final MitarbeiterRepository mitarbeiterRepository;

    private final RolleRepository rolleRepository;

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

    public String getDsgvoFilepath() {
        final File ablageverzeichnis = new File(getAblageverzeichnis().getWert());
        return new File(new File(ablageverzeichnis.getParent()), DSGVO_FILENAME).getAbsolutePath();
    }

    public Einstellungen getFiliale() {
        return getEinstellung("reparatur.filiale");
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

    public List<DefaultKeyValue<Integer, String>> listFiliale() {
        final List<DefaultKeyValue<Integer, String>> result = new ArrayList<>();
        filialeRepository.findAllByOrderByNameAsc().forEach(f -> result.add(new DefaultKeyValue<>(f.getId(), f.getName())));
        return result;
    }

    public Page<Filiale> listFiliale(final PageRequest pageRequest) {
        return filialeRepository.findAll(pageRequest);
    }

    public Optional<Filiale> getFiliale(final Integer id) {
        return filialeRepository.findById(id);
    }

    public Filiale save(final Filiale filiale) {
        return filialeRepository.save(filiale);
    }

    public Page<MitarbeiterDTO> listMitarbeiter(final PageRequest pageRequest) {
        return mitarbeiterRepository.findAll(pageRequest);
    }

    public List<DefaultKeyValue<Integer, String>> getMitarbeiter() {
        final List<DefaultKeyValue<Integer, String>> result = new ArrayList<>();
        mitarbeiterRepository.findAllByOrderByNachnameAsc().forEach(m -> result.add(new DefaultKeyValue<>(m.getId(), m.getCompleteName())));
        return result;
    }

    public Optional<Mitarbeiter> getMitarbeiter(final Integer id) {
        return mitarbeiterRepository.findById(id);
    }

    public void save(final Mitarbeiter mitarbeiter) {
        if (mitarbeiter.getBenutzername() == null) {
            mitarbeiter.setBenutzername(generateDefaultBenutzername(mitarbeiter));
        }
        if (mitarbeiter.getPasswort() == null) {
            mitarbeiter.setPasswort(hashPassword(mitarbeiter.getBenutzername()));
        }

        mitarbeiterRepository.save(mitarbeiter);
    }

    public String hashPassword(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private String generateDefaultBenutzername(final Mitarbeiter mitarbeiter) {
        String benutzername = StringUtils.left(mitarbeiter.getVorname(), 1).toLowerCase() + mitarbeiter.getNachname().toLowerCase();
        benutzername = StringUtils.rightPad(benutzername, Mitarbeiter.MIN_LENGTH_BENUTZERNAME, "0");
        benutzername = StringUtils.left(benutzername, Mitarbeiter.MAX_LENGTH_BENUTZERNAME);
        return benutzername;
    }

    public boolean existsUsername(final String benutzername) {
        return mitarbeiterRepository.findByBenutzername(StringUtils.trimToEmpty(benutzername)).isPresent();
    }

    public List<Rolle> listRollen() {
        return Lists.newArrayList(rolleRepository.findAll());
    }

}
