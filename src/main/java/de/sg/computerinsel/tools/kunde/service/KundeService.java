package de.sg.computerinsel.tools.kunde.service;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.kunde.dao.KundeRepository;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KundeService {

    private final KundeRepository kundeRepository;

    public Page<Kunde> listKunden(final PageRequest pagination, final Map<String, String> conditions) {
        final String nachname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nachname");
        final String vorname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "vorname");
        final String plz = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "plz");

        if (StringUtils.isBlank(nachname) && StringUtils.isBlank(vorname) && StringUtils.isBlank(plz)) {
            return kundeRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<Kunde> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(kundeRepository, pagination, buildMethodnameForQueryKunde(nachname, vorname, plz), vorname, plz,
                    nachname, nachname);
        }
    }

    private String buildMethodnameForQueryKunde(final String nachname, final String vorname, final String plz) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(vorname)) {
            methodName += "VornameLikeAnd";
        }
        if (StringUtils.isNotBlank(plz)) {
            methodName += "PlzLikeAnd";
        }
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "NachnameLikeOrFirmennameLikeAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public Optional<Kunde> getKunde(final Integer id) {
        return kundeRepository.findById(id);
    }

    public void saveDsgvo(final Integer id) {
        getKunde(id).ifPresent(k -> {
            k.setDsgvo(true);
            save(k);
        });
    }

    public Kunde save(final Kunde kunde) {
        if (kunde.getNummer() == null) {
            kunde.setNummer(kundeRepository.getNextNummer());
        }
        return kundeRepository.save(kunde);
    }

    public void deleteKunde(final Integer id) {
        kundeRepository.deleteById(id);
    }

}
