package de.sg.computerinsel.tools.rechnung.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.rechnung.model.Rechnung;

public interface RechnungRepository extends CrudRepository<Rechnung, Integer> {

    Page<Rechnung> findAll(Pageable pagination);

}
