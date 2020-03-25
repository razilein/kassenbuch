package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.VKunde;

public interface VKundeRepository extends CrudRepository<VKunde, Integer> {

    Page<VKunde> findAll(Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLikeAndSuchfeld2TelefonLike(String nachname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLike(String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLike(String vorname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLike(String vorname, String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLike(String nachname, String plz, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String nachname, String firmenname, String plz,
            String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String nachname, String plz,
            String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLike(String vorname, String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndSuchfeld2TelefonLike(String vorname, String nachname,
            String firmenname, String telefon, Pageable pagination);

    Page<VKunde> findByPlzLike(String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLike(String vorname, String plz, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String firmenname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndSuchfeld2TelefonLike(String firmenname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLike(String nachname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLike(String vorname, String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLike(String vorname, String nachname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLike(String vorname, String nachname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String firmenname, String plz,
            String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndSuchfeld2TelefonLike(String nachname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLike(String nachname, String firmenname, String plz, Pageable pagination);

    Page<VKunde> findBySuchfeld2TelefonLike(String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String nachname,
            String firmenname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLike(String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLike(String vorname, String nachname, String firmenname, String plz,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndSuchfeld2TelefonLike(String vorname, String nachname, String telefon,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndSuchfeld2TelefonLike(String vorname, String firmenname, String telefon,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndSuchfeld2TelefonLike(String vorname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndSuchfeld2TelefonLike(String nachname, String firmenname, String telefon,
            Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLike(String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByPlzLikeAndSuchfeld2TelefonLike(String plz, String telefon, Pageable pagination);

}
