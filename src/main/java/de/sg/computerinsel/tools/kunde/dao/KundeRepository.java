package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.Kunde;

public interface KundeRepository extends CrudRepository<Kunde, Integer> {

    Page<Kunde> findAll(Pageable pagination);

    Page<Kunde> findByVornameLikeAndPlzLikeAndNachnameLikeAndFirmennameLike(String vorname, String plz, String nachname, String firmenname,
            Pageable pagination);

    Page<Kunde> findByVornameLikeAndPlzLikeAndFirmennameLike(String vorname, String plz, String firmenname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndPlzLikeAndNachnameLike(String vorname, String plz, String nachname, Pageable pagination);

    Page<Kunde> findByPlzLikeAndNachnameLikeAndFirmennameLike(String plz, String nachname, String firmenname, Pageable pagination);

    Page<Kunde> findByPlzLikeAndFirmennameLike(String plz, String firmenname, Pageable pagination);

    Page<Kunde> findByPlzLikeAndNachnameLike(String plz, String nachname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndNachnameLikeAndFirmennameLike(String vorname, String nachname, String firmenname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndNachnameLike(String vorname, String nachname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndFirmennameLike(String vorname, String firmenname, Pageable pagination);

    Page<Kunde> findByVornameLikeAndPlzLike(String vorname, String plz, Pageable pagination);

    Page<Kunde> findByNachnameLikeAndFirmennameLike(String nachname, String firmenname, Pageable pagination);

    Page<Kunde> findByNachnameLike(String nachname, Pageable pagination);

    Page<Kunde> findByFirmennameLike(String firmenname, Pageable pagination);

    Page<Kunde> findByVornameLike(String vorname, Pageable pagination);

    Page<Kunde> findByPlzLike(String plz, Pageable pagination);

    @Query(value = "call NEXT VALUE FOR K_NUMMER_SEQUENCE", nativeQuery = true)
    Integer getNextNummer();

}
