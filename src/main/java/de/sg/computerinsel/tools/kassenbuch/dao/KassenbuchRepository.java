package de.sg.computerinsel.tools.kassenbuch.dao;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuch;

public interface KassenbuchRepository extends CrudRepository<Kassenbuch, Integer> {

    Page<Kassenbuch> findAll(Pageable pagination);

    Page<Kassenbuch> findByDatum(LocalDate datum, Pageable pagination);

}
