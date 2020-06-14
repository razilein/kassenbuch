package de.sg.computerinsel.tools.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.dao.ProtokollRepository;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchDTO;
import de.sg.computerinsel.tools.model.Protokoll;
import de.sg.computerinsel.tools.model.Protokoll.Protokolltabelle;
import de.sg.computerinsel.tools.model.Protokoll.Protokolltyp;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.util.CurrencyUtils;
import de.sg.computerinsel.tools.util.DateUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProtokollService {

    private final MitarbeiterService mitarbeiterService;

    private final ProtokollRepository protokollRepository;

    public void write(final Integer id, final Protokolltabelle tabelle, final Protokolltyp typ) {
        write(id, tabelle, null, typ);
    }

    public void write(final Integer id, final Protokolltabelle tabelle, final String bemerkung, final Protokolltyp typ) {
        protokollRepository.save(new Protokoll(id, tabelle, bemerkung, getMitarbeiter(), typ));
    }

    public void write(final String bemerkung) {
        protokollRepository.save(new Protokoll(bemerkung, getMitarbeiter()));
    }

    private String getMitarbeiter() {
        return mitarbeiterService.getAngemeldeterMitarbeiter().map(Mitarbeiter::getCompleteName)
                .orElseGet(() -> SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public static String getBezeichnungKassenbuch(final KassenbuchDTO dto) {
        return "Ausgangsbetrag: ".concat(DateUtils.format(dto.getKassenbuch().getDatum())).concat(", Ausgangsbetrag: ")
                .concat(CurrencyUtils.format(dto.getKassenbuch().getAusgangsbetrag()));
    }

}
