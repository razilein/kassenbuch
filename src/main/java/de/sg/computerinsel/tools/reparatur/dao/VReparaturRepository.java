package de.sg.computerinsel.tools.reparatur.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.VReparatur;

public interface VReparaturRepository extends CrudRepository<VReparatur, Integer> {

    Page<VReparatur> findAll(Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLikeAndReparaturNrLikeAndErledigtAndKundeNrLike(String suchfeldName, String reparaturNr,
            Boolean erledigt, String kundeNr, Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLikeAndReparaturNrLikeAndErledigt(String suchfeldName, String reparaturNr, Boolean erledigt,
            Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLikeAndReparaturNrLikeAndKundeNrLike(String suchfeldName, String reparaturNr, String kundeNr,
            Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLikeAndReparaturNrLike(String suchfeldName, String reparaturNr, Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLikeAndErledigtAndKundeNrLike(String suchfeldName, Boolean erledigt, String kundeNr,
            Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLikeAndErledigt(String suchfeldName, Boolean erledigt, Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLikeAndKundeNrLike(String suchfeldName, String kundeNr, Pageable pagination);

    Page<VReparatur> findByKundeSuchfeldNameLike(String suchfeldName, Pageable pagination);

    Page<VReparatur> findByReparaturNrLikeAndErledigtAndKundeNrLike(String reparaturNr, Boolean erledigt, String kundeNr,
            Pageable pagination);

    Page<VReparatur> findByReparaturNrLikeAndErledigt(String reparaturNr, Boolean erledigt, Pageable pagination);

    Page<VReparatur> findByReparaturNrLike(String reparaturNr, Pageable pagination);

    Page<VReparatur> findByReparaturNrLikeAndKundeNrLike(String reparaturNr, String kundeNr, Pageable pagination);

    Page<VReparatur> findByErledigtAndKundeNrLike(Boolean erledigt, String kundeNr, Pageable pagination);

    Page<VReparatur> findByErledigt(Boolean erledigt, Pageable pagination);

    Page<VReparatur> findByKundeNrLike(String kundeNr, Pageable pagination);

    Page<VReparatur> findByKundeId(Integer kundeId, Pageable pagination);

}
