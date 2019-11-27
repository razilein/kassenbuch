package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.Kunde;

public interface KundeRepository extends CrudRepository<Kunde, Integer> {

    @Query(value = "call NEXT VALUE FOR K_NUMMER_SEQUENCE", nativeQuery = true)
    Integer getNextNummer();

}
