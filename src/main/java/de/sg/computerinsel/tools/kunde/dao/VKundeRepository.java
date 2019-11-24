package de.sg.computerinsel.tools.kunde.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.kunde.model.VKunde;

public interface VKundeRepository extends CrudRepository<VKunde, Integer> {

    Page<VKunde> findAll(Pageable pagination);

    Page<VKunde> findBySuchfeldNameLike(String suchfeldName, Pageable pagination);

    Page<VKunde> findByPlzLike(String plz, Pageable pagination);

    Page<VKunde> findBySuchfeldNameLikeAndPlzLike(String suchfeldName, Pageable pagination);

}
