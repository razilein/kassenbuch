package de.sg.computerinsel.tools.inventar.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.inventar.dao.GruppeRepository;
import de.sg.computerinsel.tools.inventar.dao.KategorieRepository;
import de.sg.computerinsel.tools.inventar.dao.ProduktRepository;
import de.sg.computerinsel.tools.inventar.model.Gruppe;
import de.sg.computerinsel.tools.inventar.model.Kategorie;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventarService {

    private final GruppeRepository gruppeRepository;

    private final KategorieRepository kategorieRepository;

    private final ProduktRepository produktRepository;

    public List<DefaultKeyValue<Integer, String>> listKategorien() {
        return kategorieRepository.findAllByOrderByBezeichnungAsc().stream().map(k -> new DefaultKeyValue<>(k.getId(), k.getBezeichnung()))
                .collect(Collectors.toList());
    }

    public Page<Kategorie> listKategorien(final PageRequest pagination, final Map<String, String> conditions) {
        final String bezeichnung = SearchQueryUtils.getAndReplaceJoker(conditions, "bezeichnung");

        if (StringUtils.isNotBlank(bezeichnung)) {
            return kategorieRepository.findAllByBezeichnungLike(pagination, bezeichnung);
        }
        return kategorieRepository.findAll(pagination);
    }

    public Optional<Kategorie> getKategorie(final Integer id) {
        return kategorieRepository.findById(id);
    }

    public Kategorie saveKategorie(final Kategorie kategorie) {
        return kategorieRepository.save(kategorie);
    }

    public void deleteKategorie(final Integer id) {
        kategorieRepository.deleteById(id);
    }

    public List<DefaultKeyValue<Integer, String>> listGruppen(final Integer kategorieId) {
        return gruppeRepository.findAllByKategorieIdOrderByBezeichnungAsc(kategorieId).stream()
                .map(k -> new DefaultKeyValue<>(k.getId(), k.getBezeichnung())).collect(Collectors.toList());
    }

    public Page<Gruppe> listGruppen(final PageRequest pagination, final Map<String, String> conditions) {
        final String bezeichnung = SearchQueryUtils.getAndReplaceJoker(conditions, "bezeichnung");
        final String kategorieId = conditions.get("kategorie");

        if (StringUtils.isNumeric(kategorieId) && StringUtils.isNotBlank(bezeichnung)) {
            return gruppeRepository.findAllByBezeichnungLikeAndKategorieId(bezeichnung, Ints.tryParse(kategorieId), pagination);
        } else if (StringUtils.isNumeric(kategorieId)) {
            return gruppeRepository.findAllByKategorieId(Ints.tryParse(kategorieId), pagination);
        } else if (StringUtils.isNotBlank(bezeichnung)) {
            return gruppeRepository.findAllByBezeichnungLike(bezeichnung, pagination);
        }
        return gruppeRepository.findAll(pagination);
    }

    public Optional<Gruppe> getGruppe(final Integer id) {
        return gruppeRepository.findById(id);
    }

    public Gruppe saveGruppe(final Gruppe gruppe) {
        return gruppeRepository.save(gruppe);
    }

    public void deleteGruppe(final Integer id) {
        gruppeRepository.deleteById(id);
    }

    public Page<Produkt> listProdukte(final PageRequest pagination, final Map<String, String> conditions) {
        final String bezeichnung = SearchQueryUtils.getAndReplaceJoker(conditions, "bezeichnung");
        final String kategorieId = conditions.get("kategorie");
        final String gruppeId = conditions.get("gruppe");
        final String ean = conditions.get("ean");

        if (!StringUtils.isNumeric(kategorieId) && !StringUtils.isNumeric(gruppeId) && StringUtils.isBlank(bezeichnung)
                && StringUtils.isBlank(ean)) {
            return produktRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<Produkt> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(produktRepository, pagination,
                    buildMethodnameForQueryProdukt(bezeichnung, kategorieId, gruppeId, ean), bezeichnung,
                    StringUtils.isNumeric(kategorieId) ? Ints.tryParse(kategorieId) : null,
                    StringUtils.isNumeric(gruppeId) ? Ints.tryParse(gruppeId) : null, ean);
        }
    }

    private String buildMethodnameForQueryProdukt(final String bezeichnung, final String kategorieId, final String gruppeId,
            final String ean) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(bezeichnung)) {
            methodName += "BezeichnungLikeAnd";
        }
        if (StringUtils.isNumeric(kategorieId)) {
            methodName += "GruppeKategorieIdAnd";
        }
        if (StringUtils.isNumeric(gruppeId)) {
            methodName += "GruppeIdAnd";
        }
        if (StringUtils.isNotBlank(ean)) {
            methodName += "EanAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public Optional<Produkt> getProdukt(final Integer id) {
        return produktRepository.findById(id);
    }

    public Produkt saveProdukt(final Produkt produkt) {
        return produktRepository.save(produkt);
    }

    public void deleteProdukt(final Integer id) {
        produktRepository.deleteById(id);
    }

    public void bestandReduzieren(final List<Rechnungsposten> postenList) {
        for (final Rechnungsposten posten : postenList.stream().filter(InventarService::isBegrenzterBestand).collect(Collectors.toList())) {
            final Optional<Produkt> optional = getProdukt(posten.getProdukt().getId());
            if (optional.isPresent()) {
                final Produkt produkt = optional.get();
                final int bestand = produkt.getBestand() - posten.getMenge();
                produkt.setBestand(bestand < 0 ? 0 : bestand);
                saveProdukt(produkt);
            }
        }
    }

    private static boolean isBegrenzterBestand(final Rechnungsposten p) {
        return p.getProdukt() != null && p.getProdukt().getId() != null && !p.getProdukt().isBestandUnendlich();
    }

}
