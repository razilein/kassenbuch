package de.sg.computerinsel.tools.inventar.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.inventar.model.Produkt;

public interface ProduktRepository extends CrudRepository<Produkt, Integer> {

    Page<Produkt> findAll(Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieIdAndGruppeId(String bezeichnung, Integer kategorieId, Integer gruppeId,
            Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieIdAndGruppeIdAndHerstellerLike(String bezeichnung, Integer kategorieId,
            Integer gruppeId, String hersteller, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieId(String bezeichnung, Integer kategorieId, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieIdAndHerstellerLike(String bezeichnung, Integer kategorieId, String hersteller,
            Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeId(String bezeichnung, Integer gruppeId, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeIdAndHerstellerLike(String bezeichnung, Integer gruppeId, String hersteller,
            Pageable pagination);

    Page<Produkt> findByBezeichnungLike(String bezeichnung, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndHerstellerLike(String bezeichnung, String hersteller, Pageable pagination);

    Page<Produkt> findByGruppeKategorieId(Integer kategorieId, Pageable pagination);

    Page<Produkt> findByGruppeKategorieIdAndHerstellerLike(Integer kategorieId, String hersteller, Pageable pagination);

    Page<Produkt> findByGruppeKategorieIdAndGruppeId(Integer kategorieId, Integer gruppeId, Pageable pagination);

    Page<Produkt> findByGruppeKategorieIdAndGruppeIdAndHerstellerLike(Integer kategorieId, Integer gruppeId, String hersteller,
            Pageable pagination);

    Page<Produkt> findByGruppeId(Integer gruppeId, Pageable pagination);

    Page<Produkt> findByGruppeIdAndHerstellerLike(Integer gruppeId, String hersteller, Pageable pagination);

    Page<Produkt> findByHerstellerLike(String hersteller, Pageable pagination);

    Page<Produkt> findByEan(String ean, Pageable pagination);

    List<Produkt> findByEan(String ean);

    List<Produkt> findByBestandGreaterThanAndBestandUnendlichOrderByBezeichnungAsc(int bestand, boolean bestandUnendlich);

    List<Produkt> findByAenderungsdatumAfterAndAenderungsdatumBeforeAndEanIsNotNull(LocalDateTime aendarungsdatumVon,
            LocalDateTime aenderungsdatumBis);

}
