package de.sg.computerinsel.tools.reparatur.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.reparatur.model.Kunde;

public interface KundeRepository extends CrudRepository<Kunde, Integer> {

    Page<Kunde> findAll(Pageable pagination);

    Page<Kunde> findByNachnameLikeAndVornameLikeAndPlzLike(String nachname, String vorname, String plz, Pageable pagination);

    Page<Kunde> findByNachnameLikeAndPlzLike(String nachname, String plz, Pageable pagination);

    Page<Kunde> findByNachnameLikeAndVornameLike(String nachname, String vorname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndPlzLike(String vorname, String plz, Pageable pagination);

    Page<Kunde> findByNachnameLike(String nachname, Pageable pagination);

    Page<Kunde> findByVornameLike(String vorname, Pageable pagination);

    Page<Kunde> findByPlzLike(String plz, Pageable pagination);

    @Query(value = "call NEXT VALUE FOR K_NUMMER_SEQUENCE", nativeQuery = true)
    Integer getNextNummer();

}
