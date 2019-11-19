package de.sg.computerinsel.tools.kunde.service;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.sg.computerinsel.tools.kunde.dao.KundeRepository;
import de.sg.computerinsel.tools.kunde.dao.VKundeRepository;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.model.KundeDuplikatDto;
import de.sg.computerinsel.tools.kunde.model.VKunde;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KundeService {

    private final KundeRepository kundeRepository;

    private final VKundeRepository vKundeRepository;

    private final RechnungService rechnungService;

    private final ReparaturService reparaturService;

    public Page<VKunde> listKunden(final PageRequest pagination, final Map<String, String> conditions) {
        final String firmenname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "firmenname");
        final String nachname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nachname");
        final String vorname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "vorname");
        final String plz = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "plz");

        if (StringUtils.isBlank(firmenname) && StringUtils.isBlank(nachname) && StringUtils.isBlank(vorname) && StringUtils.isBlank(plz)) {
            return vKundeRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<VKunde> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vKundeRepository, pagination, buildMethodnameForQueryKunde(vorname, plz, nachname, firmenname),
                    vorname, plz, nachname, firmenname);
        }
    }

    private String buildMethodnameForQueryKunde(final String vorname, final String plz, final String nachname, final String firmenname) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(vorname)) {
            methodName += "VornameLikeAnd";
        }
        if (StringUtils.isNotBlank(plz)) {
            methodName += "PlzLikeAnd";
        }
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "NachnameLikeAnd";
        }
        if (StringUtils.isNotBlank(firmenname)) {
            methodName += "FirmennameLikeAnd";
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

    @Transactional
    public void duplikatZusammenfuehren(final KundeDuplikatDto dto) {
        rechnungService.duplikateZusammenfuehren(dto);
        reparaturService.duplikateZusammenfuehren(dto);
        deleteKunde(dto.getDuplikat().getId());
    }

}
