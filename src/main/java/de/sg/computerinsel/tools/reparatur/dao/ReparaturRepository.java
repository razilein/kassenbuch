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

    Page<Reparatur> findByKundeNachnameLikeAndNummerLikeAndErledigtAndKundeNummer(String nachname, String nummer, Boolean erledigt,
            Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByKundeNachnameLikeAndNummerLikeAndErledigt(String nachname, String nummer, Boolean erledigt, Pageable pagination);

    Page<Reparatur> findByKundeNachnameLikeAndNummerLikeAndKundeNummer(String nachname, String nummer, Integer kundennummer,
            Pageable pagination);

    Page<Reparatur> findByKundeNachnameLikeAndNummerLike(String nachname, String nummer, Pageable pagination);

    Page<Reparatur> findByKundeNachnameLikeAndErledigtAndKundeNummer(String nachname, Boolean erledigt, Integer kundennummer,
            Pageable pagination);

    Page<Reparatur> findByKundeNachnameLikeAndErledigt(String nachname, Boolean erledigt, Pageable pagination);

    Page<Reparatur> findByKundeNachnameLikeAndKundeNummer(String nachname, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByKundeNachnameLike(String nachname, Pageable pagination);

    Page<Reparatur> findByNummerLikeAndErledigtAndKundeNummer(String nummer, Boolean erledigt, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByNummerLikeAndErledigt(String nummer, Boolean erledigt, Pageable pagination);

    Page<Reparatur> findByNummerLikeAndKundeNummer(String nummer, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByNummerLike(String nummer, Pageable pagination);

    Page<Reparatur> findByNummerLikeAndKundeNummerLike(String nummer, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByErledigtAndKundeNummer(Boolean erledigt, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByErledigt(Boolean erledigt, Pageable pagination);

    Page<Reparatur> findByErledigtAndKundeNummerLike(Boolean erledigt, Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByKundeNummer(Integer kundennummer, Pageable pagination);

    Page<Reparatur> findByKundeId(Integer kundeId, Pageable pagination);

    @Query(value = "SELECT abholdatum FROM reparatur WHERE erledigt = 0 GROUP BY abholdatum HAVING COUNT(abholdatum) >= 5", nativeQuery = true)
    List<Date> listDaysWithMin5AbholungenAndAuftragNotErledigt();

}
