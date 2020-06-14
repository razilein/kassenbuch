package de.sg.computerinsel.tools.angebot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.angebot.dao.AngebotRepository;
import de.sg.computerinsel.tools.angebot.dao.VAngebotRepository;
import de.sg.computerinsel.tools.angebot.dto.AngebotDto;
import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.angebot.model.Angebotsposten;
import de.sg.computerinsel.tools.angebot.model.VAngebot;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.util.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.util.SearchQueryUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AngebotService {

    private static final int LAENGE_ANGEBOTNUMMER_JAHR = 2;

    private final AngebotRepository angebotRepository;

    private final AngebotspostenRepository angebotspostenRepository;

    private final VAngebotRepository vAngebotRepository;

    private final EinstellungenService einstellungenService;

    private final MitarbeiterService mitarbeiterService;

    public Page<VAngebot> listAngebote(final PageRequest pagination, final Map<String, String> conditions) {
        final String name = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "suchfeld_name");
        final String nummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nummer");
        final String kundennummer = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "kundennummer");
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtErledigt = BooleanUtils.toBoolean(conditions.get("erledigt"));
        final String bezeichnung = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "bezeichnung");

        if (StringUtils.isNumeric(kundeId)) {
            return vAngebotRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (StringUtils.isBlank(name) && StringUtils.isBlank(nummer) && StringUtils.isBlank(bezeichnung) && !istNichtErledigt
                && StringUtils.isBlank(kundennummer)) {
            return vAngebotRepository.findAll(pagination);
        } else if (StringUtils.isNotBlank(bezeichnung)) {
            return vAngebotRepository.findByPostenBezeichnungLike(bezeichnung, pagination);
        } else if (istNichtErledigt) {
            final FindAllByConditionsExecuter<VAngebot> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vAngebotRepository, pagination,
                    buildMethodnameForQueryAngebot(name, nummer, kundennummer, istNichtErledigt), name, nummer, kundennummer,
                    !istNichtErledigt);
        } else {
            final FindAllByConditionsExecuter<VAngebot> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vAngebotRepository, pagination, buildMethodnameForQueryAngebot(name, nummer, kundennummer, false),
                    name, nummer, kundennummer);
        }
    }

    private String buildMethodnameForQueryAngebot(final String name, final String nummer, final String kundennummer,
            final boolean istNichtErledigt) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(name)) {
            methodName += "KundeSuchfeldNameLikeAnd";
        }
        if (StringUtils.isNotBlank(nummer)) {
            methodName += "AngebotNrLikeAnd";
        }
        if (StringUtils.isNotBlank(kundennummer)) {
            methodName += "KundeNrLikeAnd";
        }
        if (istNichtErledigt) {
            methodName += "ErledigtAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public AngebotDto getAngebot(final Integer id) {
        final AngebotDto dto;
        final Optional<Angebot> optional = angebotRepository.findById(id);
        if (optional.isPresent()) {
            final Angebot angebot = optional.get();
            dto = new AngebotDto(angebot, getAngebotsposten(angebot.getId()));
        } else {
            dto = new AngebotDto(einstellungenService.getMwstProzent());
        }
        return dto;
    }

    public List<Angebotsposten> getAngebotsposten(final Integer angebotId) {
        return angebotspostenRepository.findByAngebotIdOrderByPositionAsc(angebotId);
    }

    public Angebot save(final Angebot angebot) {
        final boolean isErstellen = angebot.getId() == null;
        if (isErstellen) {
            angebot.setErstelltAm(LocalDateTime.now());
            if (StringUtils.isBlank(angebot.getErsteller())) {
                angebot.setErsteller(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
            }
            angebot.setErsteller(StringUtils.abbreviate(angebot.getErsteller(), Reparatur.MAX_LENGTH_MITARBEITER));
            angebot.setFiliale(mitarbeiterService.getAngemeldeterMitarbeiterFiliale().orElseGet(Filiale::new));
            final String nummer = getAngebotJahrZweistellig() + mitarbeiterService.getAndSaveNextAngebotsnummer();
            angebot.setNummer(Ints.tryParse(nummer));
        }
        return angebotRepository.save(angebot);
    }

    private String getAngebotJahrZweistellig() {
        return StringUtils.right(String.valueOf(LocalDate.now().getYear()), LAENGE_ANGEBOTNUMMER_JAHR);
    }

    public void savePosten(final List<Angebotsposten> posten) {
        angebotspostenRepository.saveAll(posten);
    }

    public void angebotErledigen(final Angebot angebot, final boolean erledigt) {
        angebot.setErledigt(erledigt);
        angebot.setErledigungsdatum(erledigt ? LocalDateTime.now() : null);
        save(angebot);
    }

    public void deleteAngebot(final Integer id) {
        angebotRepository.deleteById(id);
    }

    public void deleteAngebotsposten(final Integer id) {
        angebotspostenRepository.deleteById(id);
    }

}
