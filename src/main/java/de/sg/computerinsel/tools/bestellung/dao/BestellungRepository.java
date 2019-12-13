package de.sg.computerinsel.tools.bestellung.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.bestellung.model.Bestellung;

public interface BestellungRepository extends CrudRepository<Bestellung, Integer> {

    Page<Bestellung> findAll(Pageable pagination);

    Page<Bestellung> findByKundeId(Integer kundeId, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNummerAndErledigtAndNummer(String suchfeldName, String beschreibung,
            Integer kundeId, Boolean erledigt, Integer nummer, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNummerAndErledigt(String suchfeldName, String beschreibung,
            Integer kundeId, Boolean erledigt, Pageable pagination);

    Page<Bestellung> findByBeschreibungLikeAndKundeNummerAndErledigtAndNummer(String beschreibung, Integer kundeId, Boolean erledigt,
            Integer nummer, Pageable pagination);

    Page<Bestellung> findByBeschreibungLikeAndKundeNummerAndErledigt(String beschreibung, Integer kundeId, Boolean erledigt,
            Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNummerAndNummer(String suchfeldName, String beschreibung,
            Integer kundeId, Integer nummer, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNummer(String suchfeldName, String beschreibung, Integer kundeId,
            Pageable pagination);

    Page<Bestellung> findByBeschreibungLikeAndKundeNummerAndNummer(String beschreibung, Integer kundeId, Integer nummer, Pageable pagination);

    Page<Bestellung> findByBeschreibungLikeAndKundeNummer(String beschreibung, Integer kundeId, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndNummer(String suchfeldName, String beschreibung, Integer nummer,
            Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLike(String suchfeldName, String beschreibung, Pageable pagination);

    Page<Bestellung> findByBeschreibungLikeAndNummer(String beschreibung, Integer nummer, Pageable pagination);

    Page<Bestellung> findByBeschreibungLike(String beschreibung, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndErledigtAndNummer(String suchfeldName, String beschreibung,
            Boolean erledigt, Integer nummer, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndErledigt(String suchfeldName, String beschreibung, Boolean erledigt,
            Pageable pagination);

    Page<Bestellung> findByBeschreibungLikeAndErledigtAndNummer(String beschreibung, Boolean erledigt, Integer nummer, Pageable pagination);

    Page<Bestellung> findByBeschreibungLikeAndErledigt(String beschreibung, Boolean erledigt, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndKundeNummerAndNummer(String suchfeldName, Integer kundeId, Integer nummer,
            Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndKundeNummer(String suchfeldName, Integer kundeId, Pageable pagination);

    Page<Bestellung> findByKundeNummerAndNummer(Integer kundeId, Integer nummer, Pageable pagination);

    Page<Bestellung> findByKundeNummer(Integer kundeId, Pageable pagination);

    Page<Bestellung> findKundeSuchfeldNameLikeAndByKundeNummerAndErledigtAndNummer(String suchfeldName, Integer kundeId, Boolean erledigt,
            Integer nummer, Pageable pagination);

    Page<Bestellung> findKundeSuchfeldNameLikeAndByKundeNummerAndErledigt(String suchfeldName, Integer kundeId, Boolean erledigt,
            Pageable pagination);

    Page<Bestellung> findByKundeNummerAndErledigtAndNummer(Integer kundeId, Boolean erledigt, Integer nummer, Pageable pagination);

    Page<Bestellung> findByKundeNummerAndErledigt(Integer kundeId, Boolean erledigt, Pageable pagination);

    Page<Bestellung> findKundeSuchfeldNameLikeAndByErledigtAndNummer(String suchfeldName, Boolean erledigt, Integer nummer,
            Pageable pagination);

    Page<Bestellung> findKundeSuchfeldNameLikeAndByErledigt(String suchfeldName, Boolean erledigt, Pageable pagination);

    Page<Bestellung> findByErledigtAndNummer(Boolean erledigt, Integer nummer, Pageable pagination);

    Page<Bestellung> findByErledigt(Boolean erledigt, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLikeAndNummer(String suchfeldName, Integer nummer, Pageable pagination);

    Page<Bestellung> findByKundeSuchfeldNameLike(String suchfeldName, Pageable pagination);

    Page<Bestellung> findByNummer(Integer nummer, Pageable pagination);

}
