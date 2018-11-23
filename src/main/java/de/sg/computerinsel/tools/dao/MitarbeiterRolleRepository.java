package de.sg.computerinsel.tools.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.MitarbeiterRolle;

public interface MitarbeiterRolleRepository extends CrudRepository<MitarbeiterRolle, Integer> {

    List<MitarbeiterRolle> findByMitarbeiterId(Integer mitarbeiterId);

}
