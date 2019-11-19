package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.VKunde;

public interface VKundeRepository extends CrudRepository<VKunde, Integer> {

    Page<VKunde> findAll(Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndNachnameLikeAndFirmennameLike(String vorname, String plz, String nachname, String firmenname,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndFirmennameLike(String vorname, String plz, String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndNachnameLike(String vorname, String plz, String nachname, Pageable pagination);

    Page<VKunde> findByPlzLikeAndNachnameLikeAndFirmennameLike(String plz, String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByPlzLikeAndFirmennameLike(String plz, String firmenname, Pageable pagination);

    Page<VKunde> findByPlzLikeAndNachnameLike(String plz, String nachname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLike(String vorname, String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLike(String vorname, String nachname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLike(String vorname, String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLike(String vorname, String plz, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLike(String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByNachnameLike(String nachname, Pageable pagination);

    Page<VKunde> findByFirmennameLike(String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLike(String vorname, Pageable pagination);

    Page<VKunde> findByPlzLike(String plz, Pageable pagination);

}
