package de.sg.computerinsel.tools.stornierung.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.inventar.service.InventarService;
import de.sg.computerinsel.tools.rechnung.dao.RechnungspostenRepository;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.rechnung.rest.model.StornoDto;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import de.sg.computerinsel.tools.stornierung.dao.StornierungRepository;
import de.sg.computerinsel.tools.stornierung.dao.StornierungpostenRepository;
import de.sg.computerinsel.tools.stornierung.model.Stornierung;
import de.sg.computerinsel.tools.stornierung.model.Stornierungposten;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StornierungService {

    private static final int LAENGE_STORNIERUNGNUMMER_JAHR = 2;

    private final InventarService inventarService;

    private final MitarbeiterService mitarbeiterService;

    private final StornierungRepository stornierungRepository;

    private final StornierungpostenRepository stornierungpostenRepository;

    private final RechnungService rechnungService;

    private final RechnungspostenRepository rechnungspostenRepository;

    public Page<Stornierung> listStornierungen(final PageRequest pagination, final Map<String, String> conditions) {
        final String rechnungId = SearchQueryUtils.getAndRemoveJoker(conditions, "rechnung.id");
        return stornierungRepository.findByRechnungId(rechnungId == null ? null : Ints.tryParse(rechnungId), pagination);
    }

    @Transactional
    public void savePosten(final List<Rechnungsposten> posten, final Stornierung stornierung) {
        final List<Stornierungposten> list = posten.stream().map(p -> new Stornierungposten(stornierung, p)).collect(Collectors.toList());
        stornierungpostenRepository.saveAll(list);
        final List<Rechnungsposten> stornoPosten = posten.stream().filter(Rechnungsposten::isStorno)
                .map(p -> rechnungspostenRepository.findById(p.getId())).filter(Optional::isPresent).map(Optional::get).map(p -> {
                    p.setStorno(true);
                    return p;
                }).collect(Collectors.toList());
        rechnungspostenRepository.saveAll(stornoPosten);
        inventarService.bestandErhoehen(stornoPosten);
    }

    public Optional<Stornierung> getStornierung(final Integer id) {
        return stornierungRepository.findById(id);
    }

    public Stornierung save(final Stornierung stornierung) {
        if (stornierung.getId() == null) {
            final String nummer = getStornierungJahrZweistellig() + mitarbeiterService.getAndSaveNextStornierungnummer();
            stornierung.setNummer(Ints.tryParse(nummer));
        }
        return stornierungRepository.save(stornierung);
    }

    private String getStornierungJahrZweistellig() {
        return StringUtils.right(String.valueOf(LocalDate.now().getYear()), LAENGE_STORNIERUNGNUMMER_JAHR);
    }

    public void deleteStornierung(final Integer id) {
        stornierungRepository.deleteById(id);
    }

    public StornoDto getStorno(final Integer rechnungId) {
        final RechnungDTO rechnung = rechnungService.getRechnung(rechnungId);
        final List<Rechnungsposten> posten = rechnung.getPosten().stream().filter(p -> !p.isStorno()).collect(Collectors.toList());

        return new StornoDto(rechnung.getRechnung(), posten);
    }

    public StornoDto getStornobeleg(final Integer id) {
        final Optional<Stornierung> optional = stornierungRepository.findById(id);
        if (optional.isPresent()) {
            final Stornierung storno = optional.get();
            final RechnungDTO rechnung = rechnungService.getRechnung(storno.getRechnung().getId());
            return new StornoDto(storno, storno.isVollstorno() ? rechnung.getPosten()
                    : rechnung.getPosten().stream().filter(Rechnungsposten::isStorno).collect(Collectors.toList()));
        }
        return null;
    }

    @Transactional
    public void deleteStorno(final Integer id) {
        stornierungRepository.findById(id).ifPresent(storno -> {
            final List<Rechnungsposten> posten = getRechnungspostenByStornierung(storno.getId());
            posten.forEach(p -> {
                p.setStorno(false);
                rechnungspostenRepository.save(p);
            });
            inventarService.bestandReduzieren(posten);
            stornierungRepository.deleteById(id);
        });
    }

    private List<Rechnungsposten> getRechnungspostenByStornierung(final Integer stornoId) {
        return stornierungpostenRepository.findAllByStornierungId(stornoId).stream().map(Stornierungposten::getRechnungsposten)
                .collect(Collectors.toList());
    }

    public List<StornoDto> listStornierungenByDatum(final LocalDate datum) {
        return stornierungRepository.findAllByDatumOrderByNummerAsc(datum).stream()
                .map(storno -> new StornoDto(storno, getRechnungspostenByStornierung(storno.getId()))).collect(Collectors.toList());
    }

}
