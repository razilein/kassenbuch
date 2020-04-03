package de.sg.computerinsel.tools.bestellung.dao;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.bestellung.model.Bestellung;

public interface BestellungRepository extends CrudRepository<Bestellung, Integer> {

}
