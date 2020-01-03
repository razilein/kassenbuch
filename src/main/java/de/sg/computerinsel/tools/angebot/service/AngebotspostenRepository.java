package de.sg.computerinsel.tools.angebot.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.angebot.model.Angebotsposten;

public interface AngebotspostenRepository extends CrudRepository<Angebotsposten, Integer> {

    List<Angebotsposten> findByAngebotIdOrderByPositionAsc(Integer id);

}
