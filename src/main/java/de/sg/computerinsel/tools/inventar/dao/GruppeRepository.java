package de.sg.computerinsel.tools.inventar.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.inventar.model.Gruppe;

public interface GruppeRepository extends CrudRepository<Gruppe, Integer> {

    Page<Gruppe> findAll(Pageable pageRequest);

    List<Gruppe> findAllByKategorieIdOrderByBezeichnungAsc(Integer kategorieId);

    Page<Gruppe> findAllByBezeichnungLike(String bezeichnung, Pageable pageRequest);

    Page<Gruppe> findAllByKategorieId(Integer kategorieId, Pageable pageRequest);

    Page<Gruppe> findAllByBezeichnungLikeAndKategorieId(String bezeichnung, Integer kategorieId, Pageable pageRequest);

    List<Gruppe> findAllByKategorieBezeichnungAndBezeichnung(String kategorie, String gruppe);

}
