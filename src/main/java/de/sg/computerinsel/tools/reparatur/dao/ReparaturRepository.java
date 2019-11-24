package de.sg.computerinsel.tools.reparatur.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.Reparatur;

public interface ReparaturRepository extends CrudRepository<Reparatur, Integer> {

    Page<Reparatur> findAll(Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLikeAndNummerLikeAndErledigtAndKundeNummer(String suchfeldName, String nummer, Boolean erledigt,
            Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLikeAndNummerLikeAndErledigt(String suchfeldName, String nummer, Boolean erledigt,
            Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLikeAndNummerLikeAndKundeNummer(String suchfeldName, String nummer, Integer kundennummer,
            Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLikeAndNummerLike(String suchfeldName, String nummer, Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLikeAndErledigtAndKundeNummer(String suchfeldName, Boolean erledigt, Integer kundennummer,
            Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLikeAndErledigt(String suchfeldName, Boolean erledigt, Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLikeAndKundeNummer(String suchfeldName, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByKundeSuchfeldNameLike(String suchfeldName, Pageable pagination);

    Page<Reparatur> findByNummerLikeAndErledigtAndKundeNummer(String nummer, Boolean erledigt, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByNummerLikeAndErledigt(String nummer, Boolean erledigt, Pageable pagination);

    Page<Reparatur> findByNummerLike(String nummer, Pageable pagination);

    Page<Reparatur> findByNummerLikeAndKundeNummer(String nummer, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByErledigtAndKundeNummer(Boolean erledigt, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByErledigt(Boolean erledigt, Pageable pagination);

    Page<Reparatur> findByKundeNummer(Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByKundeId(Integer kundeId, Pageable pagination);

    List<Reparatur> findByKundeId(Integer id);

    @Query(value = "SELECT abholdatum FROM reparatur WHERE erledigt = 0 GROUP BY abholdatum HAVING COUNT(abholdatum) >= 5", nativeQuery = true)
    List<Date> listDaysWithMin5AbholungenAndAuftragNotErledigt();

}
