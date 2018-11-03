package de.sg.computerinsel.tools.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.Filiale;

public interface FilialeRepository extends CrudRepository<Filiale, Integer> {

    Page<Filiale> findAll(Pageable pageRequest);

}
