package de.sg.computerinsel.tools.rechnung.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.rechnung.model.Rechnung;

public interface RechnungRepository extends CrudRepository<Rechnung, Integer> {

    List<Rechnung> findAllByDatumAndArtOrderByNummerAsc(LocalDate datum, Integer art);

    List<Rechnung> findAllByDatumGreaterThanEqualAndDatumLessThanEqual(LocalDate datumVon, LocalDate datumBis);

    List<Rechnung> findAllByDatumGreaterThanEqualAndDatumLessThanEqualAndVorlageOrderByNummerAsc(LocalDate datumVon, LocalDate datumBis,
            Boolean vorlage);

    List<Rechnung> findAllByDatumGreaterThanEqualAndDatumLessThanEqualAndArtAndVorlageOrderByDatumAscNummerAsc(LocalDate datumVon,
            LocalDate datumBis, Integer art, Boolean vorlage);

    List<Rechnung> findByKundeId(Integer id);

    List<Rechnung> findByKundeIdAndDatumLessThanAndBezahltAndVorlage(Integer kundeId, LocalDate datum, boolean bezahlt, boolean vorlage);

}
