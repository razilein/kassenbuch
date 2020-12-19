package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.VKunde;

public interface VKundeRepository extends CrudRepository<VKunde, Integer> {

    Page<VKunde> findAll(Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLikeAndSuchfeld2TelefonLike(String nachname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String nachname, String plz, String telefon,
            String strasse, Pageable pagination);

    Page<VKunde> findByFirmennameLike(String firmenname, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndSuchfeldStrasseLike(String firmenname, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLike(String vorname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndSuchfeldStrasseLike(String vorname, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLike(String vorname, String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndSuchfeldStrasseLike(String vorname, String nachname, String firmenname,
            String strasse, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLike(String nachname, String plz, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndPlzLikeAndSuchfeldStrasseLike(String nachname, String plz, String strasse, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String nachname, String firmenname, String plz,
            String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String nachname,
            String firmenname, String plz, String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String nachname, String plz,
            String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname, String nachname,
            String plz, String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname, String plz, String telefon,
            String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLike(String vorname, String firmenname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndSuchfeldStrasseLike(String vorname, String firmenname, String strasse,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndSuchfeld2TelefonLike(String vorname, String nachname,
            String firmenname, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname,
            String nachname, String firmenname, String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByPlzLike(String plz, Pageable pagination);

    Page<VKunde> findByPlzLikeAndSuchfeldStrasseLike(String plz, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLike(String vorname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndPlzLikeAndSuchfeldStrasseLike(String vorname, String plz, String strasse, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String firmenname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String firmenname, String plz, String telefon,
            String strasse, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndSuchfeld2TelefonLike(String firmenname, String telefon, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String firmenname, String telefon, String strasse,
            Pageable pagination);

    Page<VKunde> findByNachnameLike(String nachname, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndSuchfeldStrasseLike(String nachname, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLike(String vorname, String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLikeAndSuchfeldStrasseLike(String vorname, String firmenname, String plz,
            String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLike(String vorname, String nachname, String plz, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndPlzLikeAndSuchfeldStrasseLike(String vorname, String nachname, String plz,
            String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLike(String vorname, String nachname, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndSuchfeldStrasseLike(String vorname, String nachname, String strasse,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String firmenname, String plz,
            String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname,
            String firmenname, String plz, String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndSuchfeld2TelefonLike(String nachname, String telefon, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String nachname, String telefon, String strasse,
            Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLike(String nachname, String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeldStrasseLike(String nachname, String firmenname, String plz,
            String strasse, Pageable pagination);

    Page<VKunde> findBySuchfeld2TelefonLike(String telefon, Pageable pagination);

    Page<VKunde> findBySuchfeld2TelefonLikeAndSuchfeldStrasseLike(String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLike(String vorname, String nachname,
            String firmenname, String plz, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname,
            String nachname, String firmenname, String plz, String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLike(String firmenname, String plz, Pageable pagination);

    Page<VKunde> findByFirmennameLikeAndPlzLikeAndSuchfeldStrasseLike(String firmenname, String plz, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLike(String vorname, String nachname, String firmenname, String plz,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndFirmennameLikeAndPlzLikeAndSuchfeldStrasseLike(String vorname, String nachname,
            String firmenname, String plz, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndSuchfeld2TelefonLike(String vorname, String nachname, String telefon,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndNachnameLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname, String nachname,
            String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndSuchfeld2TelefonLike(String vorname, String firmenname, String telefon,
            Pageable pagination);

    Page<VKunde> findByVornameLikeAndFirmennameLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname, String firmenname,
            String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByVornameLikeAndSuchfeld2TelefonLike(String vorname, String telefon, Pageable pagination);

    Page<VKunde> findByVornameLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String vorname, String telefon, String strasse,
            Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndSuchfeld2TelefonLike(String nachname, String firmenname, String telefon,
            Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String nachname, String firmenname,
            String telefon, String strasse, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLike(String nachname, String firmenname, Pageable pagination);

    Page<VKunde> findByNachnameLikeAndFirmennameLikeAndSuchfeldStrasseLike(String nachname, String firmenname, String strasse,
            Pageable pagination);

    Page<VKunde> findByPlzLikeAndSuchfeld2TelefonLike(String plz, String telefon, Pageable pagination);

    Page<VKunde> findByPlzLikeAndSuchfeld2TelefonLikeAndSuchfeldStrasseLike(String plz, String telefon, String strasse,
            Pageable pagination);

    Page<VKunde> findBySuchfeldStrasseLike(String strasse, Pageable pagination);

}
