package de.sg.computerinsel.tools.reparatur.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.Reparatur;

public interface ReparaturRepository extends CrudRepository<Reparatur, Integer> {

    Page<Reparatur> findAll(Pageable pagination);

    Page<Reparatur> findByKundeNachnameLikeAndNummerLike(String nachname, String nummer, Pageable pagination);

    Page<Reparatur> findByKundeNachnameLike(String nachname, Pageable pagination);

    Page<Reparatur> findByNummerLike(String nummer, Pageable pagination);

}
