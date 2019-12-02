package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.VKunde;

public interface VKundeRepository extends CrudRepository<VKunde, Integer> {

    Page<VKunde> findAll(Pageable pagination);

    Page<VKunde> findBySuchfeldNameLikeAndTelefonLike(String suchfeldName, String telefon, Pageable pagination);

    Page<VKunde> findBySuchfeldNameLike(String suchfeldName, Pageable pagination);

    Page<VKunde> findByPlzLikeAndTelefonLike(String plz, String telefon, Pageable pagination);

    Page<VKunde> findByPlzLike(String plz, Pageable pagination);

    Page<VKunde> findBySuchfeldNameLikeAndPlzLikeAndTelefonLike(String suchfeldName, String telefon, Pageable pagination);

    Page<VKunde> findBySuchfeldNameLikeAndPlzLike(String suchfeldName, Pageable pagination);

    Page<VKunde> findByTelefonLike(String telefon, Pageable pagination);

}
