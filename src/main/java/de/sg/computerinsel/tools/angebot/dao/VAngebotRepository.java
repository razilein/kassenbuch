package de.sg.computerinsel.tools.angebot.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.sg.computerinsel.tools.angebot.model.VAngebot;

public interface VAngebotRepository extends CrudRepository<VAngebot, Integer> {

    Page<VAngebot> findAll(Pageable pagination);

    Page<VAngebot> findByKundeId(Integer kundeId, Pageable pagination);

    Page<VAngebot> findByNummerAndKundeNummer(Integer nummer, Integer kundeNummer, Pageable pagination);

    Page<VAngebot> findByKundeNummer(Integer kundeNummer, Pageable pagination);

    Page<VAngebot> findByNummerAndKundeNummerAndErledigt(Integer nummer, Integer kundeNummer, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByKundeNummerAndErledigt(Integer kundeNummer, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByNummerAndErledigt(Integer nummer, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByErledigt(Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByNummer(Integer nummer, Pageable pagination);

    @Query(value = "SELECT * FROM vangebot WHERE EXISTS ( SELECT 1 FROM angebotsposten WHERE angebotsposten.angebot_id = vangebot.id AND (bezeichnung LIKE :bezeichnung))", countQuery = "SELECT COUNT(*) FROM vangebot WHERE EXISTS ( SELECT 1 FROM angebotsposten WHERE angebotsposten.angebot_id = vangebot.id AND (bezeichnung LIKE :bezeichnung))", nativeQuery = true)
    Page<VAngebot> findByPostenBezeichnungLike(@Param("bezeichnung") String bezeichnung, Pageable pagination);

}
