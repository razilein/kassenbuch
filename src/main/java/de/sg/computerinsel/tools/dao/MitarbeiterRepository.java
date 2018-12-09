package de.sg.computerinsel.tools.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.model.MitarbeiterDTO;

public interface MitarbeiterRepository extends CrudRepository<Mitarbeiter, Integer> {

    Page<MitarbeiterDTO> findAll(Pageable pageRequest);

    List<Mitarbeiter> findAllByOrderByNachnameAsc();

    Optional<Mitarbeiter> findByBenutzername(String benutzername);

}
