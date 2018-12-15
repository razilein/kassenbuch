package de.sg.computerinsel.tools.inventar.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.inventar.model.Produkt;

public interface ProduktRepository extends CrudRepository<Produkt, Integer> {

    Page<Produkt> findAll(Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieIdAndGruppeId(String bezeichnung, Integer kategorieId, Integer gruppeId,
            Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeKategorieId(String bezeichnung, Integer kategorieId, Pageable pagination);

    Page<Produkt> findByBezeichnungLikeAndGruppeId(String bezeichnung, Integer gruppeId, Pageable pagination);

    Page<Produkt> findByBezeichnungLike(String bezeichnung, Pageable pagination);

    Page<Produkt> findByGruppeKategorieId(Integer kategorieId, Pageable pagination);

    Page<Produkt> findByGruppeKategorieIdAndGruppeId(Integer kategorieId, Integer gruppeId, Pageable pagination);

    Page<Produkt> findByGruppeId(Integer gruppeId, Pageable pagination);

}