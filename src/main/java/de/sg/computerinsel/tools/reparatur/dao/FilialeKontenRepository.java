package de.sg.computerinsel.tools.reparatur.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.FilialeKonten;

public interface FilialeKontenRepository extends CrudRepository<FilialeKonten, Integer> {

    Optional<FilialeKonten> findByFilialeId(Integer filialeId);

    @Override
    List<FilialeKonten> findAll();

}
