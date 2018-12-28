package de.sg.computerinsel.tools.kassenbuch.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuchposten;

public interface KassenbuchpostenRepository extends CrudRepository<Kassenbuchposten, Integer> {

    List<Kassenbuchposten> findAllByKassenbuchId(Integer kassenbuchId);

}
