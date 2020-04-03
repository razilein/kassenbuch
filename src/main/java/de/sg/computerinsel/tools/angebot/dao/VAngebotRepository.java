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

    Page<VAngebot> findByKundeSuchfeldNameLikeAndAngebotNrLikeAndKundeNrLike(String suchfeldName, String angebotNr, String kundeNr,
            Pageable pagination);

    Page<VAngebot> findByAngebotNrLikeAndKundeNrLike(String angebotNr, String kundeNr, Pageable pagination);

    Page<VAngebot> findByKundeSuchfeldNameLikeAndKundeNrLike(String suchfeldName, String kundeNr, Pageable pagination);

    Page<VAngebot> findByKundeNrLike(String kundeNr, Pageable pagination);

    Page<VAngebot> findByKundeSuchfeldNameLikeAndAngebotNrLikeAndKundeNrLikeAndErledigt(String suchfeldName, String angebotNr,
            String kundeNr, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByAngebotNrLikeAndKundeNrLikeAndErledigt(String angebotNr, String kundeNr, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByKundeSuchfeldNameLikeAndKundeNrLikeAndErledigt(String suchfeldName, String kundeNr, Boolean erledigt,
            Pageable pagination);

    Page<VAngebot> findByKundeNrLikeAndErledigt(String kundeNr, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByKundeSuchfeldNameLikeAndAngebotNrLikeAndErledigt(String suchfeldName, String angebotNr, Boolean erledigt,
            Pageable pagination);

    Page<VAngebot> findByAngebotNrLikeAndErledigt(String angebotNr, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByKundeSuchfeldNameLikeAndErledigt(String suchfeldName, Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByErledigt(Boolean erledigt, Pageable pagination);

    Page<VAngebot> findByKundeSuchfeldNameLikeAndAngebotNrLike(String suchfeldName, String angebotNr, Pageable pagination);

    Page<VAngebot> findByAngebotNrLike(String angebotNr, Pageable pagination);

    Page<VAngebot> findByKundeSuchfeldNameLike(String suchfeldName, Pageable pagination);

    @Query(value = "SELECT * FROM vangebot WHERE EXISTS ( SELECT 1 FROM angebotsposten WHERE angebotsposten.angebot_id = vangebot.id AND (bezeichnung LIKE :bezeichnung))", countQuery = "SELECT COUNT(*) FROM vangebot WHERE EXISTS ( SELECT 1 FROM angebotsposten WHERE angebotsposten.angebot_id = vangebot.id AND (bezeichnung LIKE :bezeichnung))", nativeQuery = true)
    Page<VAngebot> findByPostenBezeichnungLike(@Param("bezeichnung") String bezeichnung, Pageable pagination);

}
