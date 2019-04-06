package de.sg.computerinsel.tools.rechnung.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.rechnung.model.Rechnung;

public interface RechnungRepository extends CrudRepository<Rechnung, Integer> {

    Page<Rechnung> findAll(Pageable pagination);

    Page<Rechnung> findByKundeId(Integer id, Pageable pagination);

    Page<Rechnung> findByNummer(Integer nummer, Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummerAndBezahlt(Integer nummer, String reparaturnummer, final Boolean bezahlt,
            Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummer(Integer nummer, String reparaturnummer, Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummerAndErstellerLikeAndBezahlt(Integer nummer, String reparaturnummer, String ersteller,
            final Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummerAndErstellerLike(Integer nummer, String reparaturnummer, String ersteller,
            Pageable pagination);

    Page<Rechnung> findByNummerAndKundeNummerAndBezahlt(Integer nummer, Integer kundenummer, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByNummerAndKundeNummer(Integer nummer, Integer kundenummer, Pageable pagination);

    Page<Rechnung> findByNummerAndKundeNummerAndErstellerLikeAndBezahlt(Integer nummer, Integer kundenummer, String ersteller,
            Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByNummerAndKundeNummerAndErstellerLike(Integer nummer, Integer kundenummer, String ersteller, Pageable pagination);

    Page<Rechnung> findByNummerAndErstellerLikeAndBezahlt(Integer nummer, String ersteller, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByNummerAndErstellerLike(Integer nummer, String ersteller, Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummerAndKundeNummerAndBezahlt(Integer nummer, String reparaturnummer, Integer kundenummer,
            final Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummerAndKundeNummer(Integer nummer, String reparaturnummer, Integer kundenummer,
            Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummerAndKundeNummerAndErstellerLikeAndBezahlt(Integer nummer, String reparaturnummer,
            Integer kundenummer, String ersteller, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByNummerAndReparaturNummerAndKundeNummerAndErstellerLike(Integer nummer, String reparaturnummer, Integer kundenummer,
            String ersteller, Pageable pagination);

    Page<Rechnung> findByReparaturNummerAndBezahlt(String reparaturnummer, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByReparaturNummer(String reparaturnummer, Pageable pagination);

    Page<Rechnung> findByReparaturNummerAndKundeNummerAndBezahlt(String reparaturnummer, Integer kundenummer, Boolean bezahlt,
            Pageable pagination);

    Page<Rechnung> findByReparaturNummerAndKundeNummer(String reparaturnummer, Integer kundenummer, Pageable pagination);

    Page<Rechnung> findByReparaturNummerAndErstellerLikeAndBezahlt(String reparaturnummer, String ersteller, Boolean bezahlt,
            Pageable pagination);

    Page<Rechnung> findByReparaturNummerAndErstellerLike(String reparaturnummer, String ersteller, Pageable pagination);

    Page<Rechnung> findByReparaturNummerAndKundeNummerAndErstellerLikeAndBezahlt(String reparaturnummer, Integer kundenummer,
            String ersteller, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByReparaturNummerAndKundeNummerAndErstellerLike(String reparaturnummer, Integer kundenummer, String ersteller,
            Pageable pagination);

    Page<Rechnung> findByKundeNummerAndBezahlt(Integer kundenummer, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByKundeNummer(Integer kundenummer, Pageable pagination);

    Page<Rechnung> findByKundeNummerAndErstellerLikeAndBezahlt(Integer kundenummer, String ersteller, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByKundeNummerAndErstellerLike(Integer kundenummer, String ersteller, Pageable pagination);

    Page<Rechnung> findByErstellerLikeAndBezahlt(String ersteller, Boolean bezahlt, Pageable pagination);

    Page<Rechnung> findByErstellerLike(String ersteller, Pageable pagination);

    Page<Rechnung> findByBezahlt(Boolean bezahlt, Pageable pagination);

    List<Rechnung> findAllByDatumAndArtOrderByNummerAsc(LocalDate datum, Integer art);

    List<Rechnung> findAllByDatumGreaterThanEqualAndDatumLessThanEqual(LocalDate datumVon, LocalDate datumBis);

    List<Rechnung> findAllByDatumGreaterThanEqualAndDatumLessThanEqualAndArtOrderByDatumAscNummerAsc(LocalDate datumVon, LocalDate datumBis,
            Integer art);
}
