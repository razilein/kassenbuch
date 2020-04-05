package de.sg.computerinsel.tools.bestellung.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.bestellung.model.VBestellung;

public interface VBestellungRepository extends CrudRepository<VBestellung, Integer> {

    Page<VBestellung> findAll(Pageable pagination);

    Page<VBestellung> findByKundeId(Integer kundeId, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNrLikeAndErledigtAndBestellungNrLike(String suchfeldName,
            String beschreibung, String kundeNr, Boolean erledigt, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNrLikeAndErledigt(String suchfeldName, String beschreibung,
            String kundeNr, Boolean erledigt, Pageable pagination);

    Page<VBestellung> findByBeschreibungLikeAndKundeNrLikeAndErledigtAndBestellungNrLike(String beschreibung, String kundeNr,
            Boolean erledigt, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByBeschreibungLikeAndKundeNrLikeAndErledigt(String beschreibung, String kundeNr, Boolean erledigt,
            Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNrLikeAndBestellungNrLike(String suchfeldName,
            String beschreibung, String kundeNr, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndKundeNrLike(String suchfeldName, String beschreibung, String kundeNr,
            Pageable pagination);

    Page<VBestellung> findByBeschreibungLikeAndKundeNrLikeAndBestellungNrLike(String beschreibung, String kundeNr, String bestellungNr,
            Pageable pagination);

    Page<VBestellung> findByBeschreibungLikeAndKundeNrLike(String beschreibung, String kundeNr, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndBestellungNrLike(String suchfeldName, String beschreibung,
            String bestellungNr, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLike(String suchfeldName, String beschreibung, Pageable pagination);

    Page<VBestellung> findByBeschreibungLikeAndBestellungNrLike(String beschreibung, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByBeschreibungLike(String beschreibung, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndErledigtAndBestellungNrLike(String suchfeldName, String beschreibung,
            Boolean erledigt, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBeschreibungLikeAndErledigt(String suchfeldName, String beschreibung, Boolean erledigt,
            Pageable pagination);

    Page<VBestellung> findByBeschreibungLikeAndErledigtAndBestellungNrLike(String beschreibung, Boolean erledigt, String bestellungNr,
            Pageable pagination);

    Page<VBestellung> findByBeschreibungLikeAndErledigt(String beschreibung, Boolean erledigt, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndKundeNrLikeAndBestellungNrLike(String suchfeldName, String kundeNr, String bestellungNr,
            Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndKundeNrLike(String suchfeldName, String kundeNr, Pageable pagination);

    Page<VBestellung> findByKundeNrLikeAndBestellungNrLike(String kundeNr, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByKundeNrLike(String kundeNr, Pageable pagination);

    Page<VBestellung> findKundeSuchfeldNameLikeAndByKundeNrLikeAndErledigtAndBestellungNrLike(String suchfeldName, String kundeNr,
            Boolean erledigt, String bestellungNr, Pageable pagination);

    Page<VBestellung> findKundeSuchfeldNameLikeAndByKundeNrLikeAndErledigt(String suchfeldName, String kundeNr, Boolean erledigt,
            Pageable pagination);

    Page<VBestellung> findByKundeNrLikeAndErledigtAndBestellungNrLike(String kundeNr, Boolean erledigt, String bestellungNr,
            Pageable pagination);

    Page<VBestellung> findByKundeNrLikeAndErledigt(String kundeNr, Boolean erledigt, Pageable pagination);

    Page<VBestellung> findKundeSuchfeldNameLikeAndByErledigtAndBestellungNrLike(String suchfeldName, Boolean erledigt, String bestellungNr,
            Pageable pagination);

    Page<VBestellung> findKundeSuchfeldNameLikeAndByErledigt(String suchfeldName, Boolean erledigt, Pageable pagination);

    Page<VBestellung> findByErledigtAndBestellungNrLike(Boolean erledigt, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByErledigt(Boolean erledigt, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLikeAndBestellungNrLike(String suchfeldName, String bestellungNr, Pageable pagination);

    Page<VBestellung> findByKundeSuchfeldNameLike(String suchfeldName, Pageable pagination);

    Page<VBestellung> findByBestellungNrLike(String bestellungNr, Pageable pagination);

}
