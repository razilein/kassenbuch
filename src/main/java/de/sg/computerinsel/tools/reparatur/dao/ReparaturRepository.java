package de.sg.computerinsel.tools.reparatur.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.Reparatur;

public interface ReparaturRepository extends CrudRepository<Reparatur, Integer> {

    List<Reparatur> findByKundeId(Integer id);

    @Query(value = "SELECT abholdatum FROM reparatur WHERE erledigt = 0 GROUP BY abholdatum HAVING COUNT(abholdatum) >= 5", nativeQuery = true)
    List<Date> listDaysWithMin5AbholungenAndAuftragNotErledigt();

}
