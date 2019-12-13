package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.VKunde;

public interface VKundeRepository extends CrudRepository<VKunde, Integer> {

    Page<VKunde> findAll(Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLikeAndTelefonLike(String nachname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLike(String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLike(String vorname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLike(String vorname, String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLike(String nachname, String plz, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLikeAndTelefonLike(String nachname, String firmenname, String plz, String telefon,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLikeAndTelefonLike(String vorname, String nachname, String plz, String telefon,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndTelefonLike(String vorname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLike(String vorname, String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndTelefonLike(String vorname, String nachname, String firmenname,
            String telefon, Pageable pagination);

    Page<VKunde> findByPlzLike(String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLike(String vorname, String plz, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLikeAndTelefonLike(String firmenname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndTelefonLike(String firmenname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLike(String nachname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLike(String vorname, String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLike(String vorname, String nachname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLike(String vorname, String nachname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLikeAndTelefonLike(String vorname, String firmenname, String plz, String telefon,
            Pageable pagination);

    Page<VKunde> findByNachnameLikeAndTelefonLike(String nachname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLike(String nachname, String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByTelefonLike(String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLikeAndTelefonLike(String vorname, String nachname,
            String firmenname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLike(String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLike(String vorname, String nachname, String firmenname, String plz,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndTelefonLike(String vorname, String nachname, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndTelefonLike(String vorname, String firmenname, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndTelefonLike(String vorname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndTelefonLike(String nachname, String firmenname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLike(String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByPlzLikeAndTelefonLike(String plz, String telefon, Pageable pagination);

}
