package de.sg.computerinsel.tools.inventar.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.inventar.model.Kategorie;

public interface KategorieRepository extends CrudRepository<Kategorie, Integer> {

    Page<Kategorie> findAll(Pageable pageRequest);

    List<Kategorie> findAllByOrderByBezeichnungAsc();

    Page<Kategorie> findAllByBezeichnungLike(Pageable pageRequest, String bezeichnung);

}
