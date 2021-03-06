package de.sg.computerinsel.tools.inventar.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.inventar.dao.GruppeRepository;
import de.sg.computerinsel.tools.inventar.dao.KategorieRepository;
import de.sg.computerinsel.tools.inventar.dao.ProduktRepository;
import de.sg.computerinsel.tools.inventar.model.Gruppe;
import de.sg.computerinsel.tools.inventar.model.Kategorie;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.inventar.model.ProduktDTO;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rest.SearchData;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventarService {

    private static final String CONDITION_DATUM_BIS = "datum_bis";

    private static final String CONDITION_DATUM_VON = "datum_von";

    private final GruppeRepository gruppeRepository;

    private final KategorieRepository kategorieRepository;

    private final ProduktRepository produktRepository;

    public List<DefaultKeyValue<Integer, String>> listKategorien() {
        return kategorieRepository.findAllByOrderByBezeichnungAsc().stream().map(k -> new DefaultKeyValue<>(k.getId(), k.getBezeichnung()))
                .collect(Collectors.toList());
    }

    public Page<Kategorie> listKategorien(final PageRequest pagination, final Map<String, String> conditions) {
        final String bezeichnung = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "bezeichnung");

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
        final String bezeichnung = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "bezeichnung");
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
        final String bezeichnung = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "bezeichnung");
        final String hersteller = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "hersteller");
        final String kategorieId = conditions.get("kategorie");
        final String gruppeId = conditions.get("gruppe");
        final String ean = StringUtils.trimToNull(conditions.get("ean"));

        if (!StringUtils.isNumeric(kategorieId) && !StringUtils.isNumeric(gruppeId) && StringUtils.isBlank(bezeichnung)
                && StringUtils.isBlank(ean) && StringUtils.isBlank(hersteller)) {
            return produktRepository.findAll(pagination);
        } else if (StringUtils.isNotBlank(ean)) {
            return produktRepository.findByEan(ean, pagination);
        } else {
            final FindAllByConditionsExecuter<Produkt> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(produktRepository, pagination,
                    buildMethodnameForQueryProdukt(bezeichnung, kategorieId, gruppeId, hersteller), bezeichnung,
                    StringUtils.isNumeric(kategorieId) ? Ints.tryParse(kategorieId) : null,
                    StringUtils.isNumeric(gruppeId) ? Ints.tryParse(gruppeId) : null, hersteller);
        }
    }

    public List<Produkt> listProdukteInAenderungszeitraum(final Map<String, LocalDate> conditions) {
        final LocalDate datumVon = conditions.get(CONDITION_DATUM_VON) == null ? LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1)
                : conditions.get(CONDITION_DATUM_VON);
        final LocalDate datumBis = conditions.get(CONDITION_DATUM_BIS) == null ? LocalDate.now().plusDays(1)
                : conditions.get(CONDITION_DATUM_BIS);
        return produktRepository.findByAenderungsdatumAfterAndAenderungsdatumBeforeAndEanIsNotNull(datumVon.atStartOfDay(),
                datumBis.plusDays(1).atStartOfDay());
    }

    private String buildMethodnameForQueryProdukt(final String bezeichnung, final String kategorieId, final String gruppeId,
            final String hersteller) {
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
        if (StringUtils.isNotBlank(hersteller)) {
            methodName += "HerstellerLikeAnd";
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
        bestandAnpassen(postenList, true);
    }

    public void bestandErhoehen(final List<Rechnungsposten> postenList) {
        bestandAnpassen(postenList, false);
    }

    private void bestandAnpassen(final List<Rechnungsposten> postenList, final boolean reduzieren) {
        for (final Rechnungsposten posten : postenList.stream().filter(InventarService::isBegrenzterBestand).collect(Collectors.toList())) {
            final Optional<Produkt> optional = getProdukt(posten.getProdukt().getId());
            if (optional.isPresent()) {
                final Produkt produkt = optional.get();
                final int bestand = reduzieren ? produkt.getBestand() - posten.getMenge() : produkt.getBestand() + posten.getMenge();
                produkt.setBestand(bestand < 0 ? 0 : bestand);
                saveProdukt(produkt);
            }
        }
    }

    private static boolean isBegrenzterBestand(final Rechnungsposten p) {
        return p.getProdukt() != null && p.getProdukt().getId() != null && !p.getProdukt().isBestandUnendlich();
    }

    public int mwstAnpassungVkNetto(final BigDecimal mwst) {
        final List<Produkt> produkte = produktRepository.findByPreisVkBruttoIsNotNull();
        produkte.stream().map(p -> {
            p.setPreisVkNetto(
                    p.getPreisVkBrutto().multiply(new BigDecimal("100")).divide(mwst.add(new BigDecimal("100")), RoundingMode.HALF_UP));
            return p;
        }).forEach(produktRepository::save);
        return produkte.size();
    }

    public void produktAnlegen(final ProduktDTO dto) {
        final Gruppe gruppe = gruppeRepository
                .findAllByKategorieBezeichnungAndBezeichnung(dto.getKategorieBezeichnung(), dto.getGruppeBezeichnung()).stream().findFirst()
                .orElseGet(() -> createGruppe(dto.getKategorieBezeichnung(), dto.getGruppeBezeichnung()));
        final Produkt produkt = new Produkt();
        produkt.setBestandUnendlich(dto.isBestandUnendlich());
        produkt.setBezeichnung(dto.getBezeichnung());
        produkt.setEan(dto.getEan());
        produkt.setGruppe(gruppe);
        produkt.setHersteller(dto.getHersteller());
        produkt.setPreisEkBrutto(dto.getPreisEkBrutto());
        produkt.setPreisEkNetto(dto.getPreisEkNetto());
        produkt.setPreisVkBrutto(dto.getPreisVkBrutto());
        produkt.setPreisVkNetto(dto.getPreisVkNetto());
        saveProdukt(produkt);
    }

    private Gruppe createGruppe(final String kategorieBezeichnung, final String gruppeBezeichnung) {
        final Kategorie kategorie = kategorieRepository.findAllByBezeichnung(kategorieBezeichnung).stream().findFirst()
                .orElseGet(() -> createKategorie(kategorieBezeichnung));
        final Gruppe gruppe = new Gruppe();
        gruppe.setBezeichnung(gruppeBezeichnung);
        gruppe.setKategorie(kategorie);
        return saveGruppe(gruppe);
    }

    private Kategorie createKategorie(final String bezeichnung) {
        final Kategorie kategorie = new Kategorie();
        kategorie.setBezeichnung(bezeichnung);
        return saveKategorie(kategorie);
    }

    public void checkAndSetSortierungAnzahlVerkaeufe(final SearchData data) {
        if (StringUtils.equals("true", data.getConditions().get("sortierung"))) {
            data.getData().setSort("anzahlVerkaeufe");
            data.getData().setSortorder(Sort.Direction.DESC.toString());
        } else if (StringUtils.equals(data.getData().getSort(), "preise")) {
            data.getData().setSort("preisVkBrutto");
        }
    }

}
