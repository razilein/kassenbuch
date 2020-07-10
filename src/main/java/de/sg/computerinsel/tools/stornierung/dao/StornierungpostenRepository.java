package de.sg.computerinsel.tools.stornierung.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.stornierung.model.Stornierungposten;

public interface StornierungpostenRepository extends CrudRepository<Stornierungposten, Integer> {

    List<Stornierungposten> findAllByStornierungId(Integer stornierungId);

}
