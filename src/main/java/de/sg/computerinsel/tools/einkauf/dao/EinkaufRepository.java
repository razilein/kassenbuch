package de.sg.computerinsel.tools.einkauf.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.einkauf.model.Einkauf;

public interface EinkaufRepository extends CrudRepository<Einkauf, Integer> {

    Optional<Einkauf> findByProduktId(Integer id);

}
