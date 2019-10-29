package de.sg.computerinsel.tools.auftrag.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.auftrag.model.Auftrag;

public interface AuftragRepository extends CrudRepository<Auftrag, Integer> {

    Page<Auftrag> findAll(Pageable pagination);

    Page<Auftrag> findByKundeId(Integer kundeId, Pageable pagination);

    Page<Auftrag> findByBeschreibungLikeAndKundeNummerAndErledigt(String beschreibung, Integer kundeId, Boolean erledigt,
            Pageable pagination);

    Page<Auftrag> findByBeschreibungLikeAndKundeNummer(String beschreibung, Integer kundeId, Pageable pagination);

    Page<Auftrag> findByBeschreibungLike(String beschreibung, Pageable pagination);

    Page<Auftrag> findByBeschreibungLikeAndErledigt(String beschreibung, Boolean erledigt, Pageable pagination);

    Page<Auftrag> findByKundeNummer(Integer kundeId, Pageable pagination);

    Page<Auftrag> findByKundeNummerAndErledigt(Integer kundeId, Boolean erledigt, Pageable pagination);

    Page<Auftrag> findByErledigt(Boolean erledigt, Pageable pagination);
}
