package de.sg.computerinsel.tools.kassenbuch.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.kassenbuch.dao.KassenbuchRepository;
import de.sg.computerinsel.tools.kassenbuch.dao.KassenbuchpostenRepository;
import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuch;
import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuchposten;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchDTO;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class KassenbuchService {

    private final EinstellungenService einstellungenService;

    private final KassenbuchRepository kassenbuchRepository;

    private final KassenbuchpostenRepository kassenbuchpostenRepository;

    private final RechnungService rechnungService;

    private final MitarbeiterService mitarbeiterService;

    public Page<Kassenbuch> listKassenbuch(final PageRequest pagination, final Map<String, String> conditions) {
        final String datum = SearchQueryUtils.getAndRemoveJoker(conditions, "datum");
        if (StringUtils.isBlank(datum)) {
            return kassenbuchRepository.findByGeloescht(false, pagination);
        } else {
            return kassenbuchRepository.findByDatumAndGeloescht(DateUtils.parse(datum, "yyyy-MM-dd"), false, pagination);
        }
    }

    public Kassenbuch saveKassenbuch(final KassenbuchDTO dto) {
        final Kassenbuch kassenbuch = kassenbuchRepository.save(dto.getKassenbuch());
        dto.getPosten().forEach(p -> p.setKassenbuch(kassenbuch));
        kassenbuchpostenRepository.saveAll(dto.getPosten());
        return kassenbuch;
    }

    public KassenbuchDTO createKassenbuch(final LocalDate datum, final BigDecimal ausgangsbetrag) {
        final Kassenbuch kassenbuch = create(datum, ausgangsbetrag);
        final List<Kassenbuchposten> posten = createPostenByBarRechnungen(datum, kassenbuch);
        return new KassenbuchDTO(kassenbuch, posten);
    }

    private List<Kassenbuchposten> createPostenByBarRechnungen(final LocalDate datum, final Kassenbuch kassenbuch) {
        return rechnungService.listBarRechnungenByDatum(datum).stream().map(r -> createPosten(kassenbuch, r)).collect(Collectors.toList());
    }

    private Kassenbuch create(final LocalDate datum, final BigDecimal ausgangsbetrag) {
        return new Kassenbuch(ausgangsbetrag, datum, mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
    }

    private Kassenbuchposten createPosten(final Kassenbuch kassenbuch, final Rechnung rechnung) {
        final String nummer = rechnung.getFiliale().getKuerzel() + rechnung.getNummerAnzeige();
        return new Kassenbuchposten(kassenbuch, nummer, rechnungService.getRechnung(rechnung.getId()).getRechnungsbetrag());
    }

    public List<Kassenbuchposten> listKassenbuchposten(final LocalDate datum) {
        return createPostenByBarRechnungen(datum, null);
    }

    public KassenbuchDTO get(final Integer id) {
        final Optional<Kassenbuch> optional = kassenbuchRepository.findById(id);
        if (optional.isPresent()) {
            return new KassenbuchDTO(optional.get(), kassenbuchpostenRepository.findAllByKassenbuchId(id));
        }
        return new KassenbuchDTO();
    }

    public void saveAusgangsbetrag(final KassenbuchDTO dto) {
        final BigDecimal betrag = dto.getKassenbuch().getAusgangsbetrag();
        final BigDecimal gesamt = dto.getPosten().stream().map(Kassenbuchposten::getBetrag).reduce(BigDecimal.ZERO, BigDecimal::add);

        mitarbeiterService.getAngemeldeterMitarbeiterFiliale().ifPresent(filiale -> {
            filiale.setAusgangsbetrag(betrag.add(gesamt));
            einstellungenService.save(filiale);
        });
    }

    public void delete(final Integer id) {
        kassenbuchRepository.findById(id).ifPresent(kassenbuch -> {
            kassenbuch.setGeloescht(true);
            kassenbuch.setLoescher(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
            kassenbuch.setDatumGeloescht(LocalDateTime.now());
            kassenbuchRepository.save(kassenbuch);
        });
    }

}
