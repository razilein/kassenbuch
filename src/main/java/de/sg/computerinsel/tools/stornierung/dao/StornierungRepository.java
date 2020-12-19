package de.sg.computerinsel.tools.stornierung.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.stornierung.model.Stornierung;

public interface StornierungRepository extends CrudRepository<Stornierung, Integer> {

    Page<Stornierung> findAll(Pageable pagination);

    Page<Stornierung> findByRechnungId(Integer id, Pageable pagination);

    List<Stornierung> findAllByDatumOrderByNummerAsc(LocalDate datum);

}
