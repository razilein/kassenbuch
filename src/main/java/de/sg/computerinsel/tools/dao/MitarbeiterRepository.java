package de.sg.computerinsel.tools.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;

public interface MitarbeiterRepository extends CrudRepository<Mitarbeiter, Integer> {

    Page<Mitarbeiter> findAll(Pageable pageRequest);

    List<Mitarbeiter> findAllByOrderByNachnameAsc();

}
