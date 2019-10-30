package de.sg.computerinsel.tools.auftrag.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.auftrag.dao.AuftragRepository;
import de.sg.computerinsel.tools.auftrag.dao.VAuftraegeJeTagRepository;
import de.sg.computerinsel.tools.auftrag.model.Auftrag;
import de.sg.computerinsel.tools.auftrag.model.VAuftraegeJeTag;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuftragService {

    private final AuftragRepository auftragRepository;

    private final VAuftraegeJeTagRepository vAuftraegeJeTagRepository;

    public List<LocalDate> listDaysWithMin5AbholungenAndAuftragNotErledigt() {
        return vAuftraegeJeTagRepository.findByAnzahlGesamtGreaterThanEqualAndDatumGreaterThanOrderByDatumAsc(5, LocalDate.now()).stream()
                .map(VAuftraegeJeTag::getDatum).collect(Collectors.toList());
    }

    public Optional<Auftrag> getAuftrag(final Integer id) {
        return auftragRepository.findById(id);
    }

    public Auftrag save(final Auftrag auftrag) {
        return auftragRepository.save(auftrag);
    }

    public void deleteAuftrag(final Integer id) {
        auftragRepository.deleteById(id);
    }

    public Auftrag auftragErledigen(final Auftrag auftrag, final boolean erledigt) {
        auftrag.setErledigt(erledigt);
        auftrag.setErledigungsdatum(erledigt ? LocalDateTime.now() : null);
        return save(auftrag);
    }

    public Page<Auftrag> listAuftraege(final PageRequest pagination, final Map<String, String> conditions) {
        String kundennummer = SearchQueryUtils.getAndRemoveJoker(conditions, "kundennummer");
        kundennummer = StringUtils.isNumeric(kundennummer) ? kundennummer : null;
        final String kundeId = SearchQueryUtils.getAndRemoveJoker(conditions, "kunde.id");
        final boolean istNichtErledigt = BooleanUtils.toBoolean(conditions.get("erledigt"));
        final String beschreibung = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "beschreibung");

        if (StringUtils.isNumeric(kundeId)) {
            return auftragRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (StringUtils.isBlank(beschreibung) && !istNichtErledigt && kundennummer == null) {
            return auftragRepository.findAll(pagination);
        } else if (istNichtErledigt) {
            final FindAllByConditionsExecuter<Auftrag> executer = new FindAllByConditionsExecuter<>();
            final Integer kdNr = kundennummer == null ? null : Ints.tryParse(kundennummer);
            return executer.findByParams(auftragRepository, pagination,
                    buildMethodnameForQueryAuftrag(beschreibung, kundennummer, istNichtErledigt), beschreibung, kdNr, !istNichtErledigt);
        } else {
            final FindAllByConditionsExecuter<Auftrag> executer = new FindAllByConditionsExecuter<>();
            final Integer kdNr = kundennummer == null ? null : Ints.tryParse(kundennummer);
            return executer.findByParams(auftragRepository, pagination, buildMethodnameForQueryAuftrag(beschreibung, kundennummer, false),
                    beschreibung, kdNr);
        }
    }

    private String buildMethodnameForQueryAuftrag(final String beschreibung, final String kundennummer, final boolean istNichtErledigt) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(beschreibung)) {
            methodName += "BeschreibungLikeAnd";
        }
        if (StringUtils.isNotBlank(kundennummer)) {
            methodName += "KundeNummerAnd";
        }
        if (istNichtErledigt) {
            methodName += "ErledigtAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

}