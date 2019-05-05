package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.Kunde;

public interface KundeRepository extends CrudRepository<Kunde, Integer> {

    Page<Kunde> findAll(Pageable pagination);

    Page<Kunde> findByVornameLikeAndPlzLikeAndNachnameLikeOrFirmennameLike(String vorname, String plz, String nachname, String firmenname,
            Pageable pagination);

    Page<Kunde> findByPlzLikeAndNachnameLikeOrFirmennameLike(String plz, String nachname, String firmenname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndNachnameLikeOrFirmennameLike(String vorname, String nachname, String firmenname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndPlzLike(String vorname, String plz, Pageable pagination);

    Page<Kunde> findByNachnameLikeOrFirmennameLike(String nachname, String firmenname, Pageable pagination);

    Page<Kunde> findByVornameLike(String vorname, Pageable pagination);

    Page<Kunde> findByPlzLike(String plz, Pageable pagination);

    @Query(value = "call NEXT VALUE FOR K_NUMMER_SEQUENCE", nativeQuery = true)
    Integer getNextNummer();

}
