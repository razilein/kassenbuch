package de.sg.computerinsel.tools.inventar.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.inventar.model.Produkt;

public interface ProduktRepository extends CrudRepository<Produkt, Integer> {

    Page<Produkt> findAll(Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieIdAndGruppeIdAndEan(String bezeichnung, Integer kategorieId, Integer gruppeId,
            String ean, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieIdAndGruppeId(String bezeichnung, Integer kategorieId, Integer gruppeId,
            Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieIdAndEan(String bezeichnung, Integer kategorieId, String ean, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieId(String bezeichnung, Integer kategorieId, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeIdAndEan(String bezeichnung, Integer gruppeId, String ean, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeId(String bezeichnung, Integer gruppeId, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndEan(String bezeichnung, String ean, Pageable pagination);

    Page<Produkt> findByBezeichnungLike(String bezeichnung, Pageable pagination);

    Page<Produkt> findByGruppeKategorieIdAndEan(Integer kategorieId, String ean, Pageable pagination);

    Page<Produkt> findByGruppeKategorieId(Integer kategorieId, Pageable pagination);

    Page<Produkt> findByGruppeKategorieIdAndGruppeIdAndEan(Integer kategorieId, Integer gruppeId, String ean, Pageable pagination);

    Page<Produkt> findByGruppeKategorieIdAndGruppeId(Integer kategorieId, Integer gruppeId, Pageable pagination);

    Page<Produkt> findByGruppeIdAndEan(Integer gruppeId, String ean, Pageable pagination);

    Page<Produkt> findByGruppeId(Integer gruppeId, Pageable pagination);

    Page<Produkt> findByEan(String ean, Pageable pagination);

    List<Produkt> findByBestandGreaterThanAndBestandUnendlichOrderByBezeichnungAsc(int bestand, boolean bestandUnendlich);

}
