package de.sg.computerinsel.tools.rechnung.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;

public interface RechnungspostenRepository extends CrudRepository<Rechnungsposten, Integer> {

    List<Rechnungsposten> findAllByRechnungIdOrderByPositionAsc(Integer rechnungId);

    List<Rechnungsposten> findAllByRechnungIdAndStornoOrderByPositionAsc(Integer id, boolean storno);

    Page<Rechnungsposten> findAllByRechnungIdOrderByPositionAsc(Integer rechnungId, Pageable pagination);

}
