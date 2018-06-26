package de.sg.computerinsel.tools.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.Einstellungen;

public interface EinstellungenRepository extends CrudRepository<Einstellungen, Integer> {

    Optional<Einstellungen> findByName(String name);

}
