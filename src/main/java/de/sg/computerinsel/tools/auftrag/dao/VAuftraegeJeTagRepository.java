package de.sg.computerinsel.tools.auftrag.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.auftrag.model.VAuftraegeJeTag;

public interface VAuftraegeJeTagRepository extends CrudRepository<VAuftraegeJeTag, Integer> {

    List<VAuftraegeJeTag> findByAnzahlGesamtGreaterThanEqualAndDatumGreaterThanOrderByDatumAsc(Integer anzahlGesamt, LocalDate datum);

}
