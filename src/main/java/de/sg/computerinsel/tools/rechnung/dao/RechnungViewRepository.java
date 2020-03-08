package de.sg.computerinsel.tools.rechnung.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.sg.computerinsel.tools.rechnung.model.RechnungView;

public interface RechnungViewRepository extends CrudRepository<RechnungView, Integer> {

    Page<RechnungView> findAll(Pageable pagination);

    Page<RechnungView> findByKundeId(Integer id, Pageable pagination);

    Page<RechnungView> findByArt(Integer art, Pageable pagination);

    Page<RechnungView> findByNummer(Integer nummer, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndBezahlt(Integer nummer, String reparaturnummer, final Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummer(Integer nummer, String reparaturnummer, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndErstellerLikeAndBezahlt(Integer nummer, String reparaturnummer, String ersteller,
            final Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndErstellerLike(Integer nummer, String reparaturnummer, String ersteller,
            Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummerAndBezahlt(Integer nummer, Integer kundenummer, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummer(Integer nummer, Integer kundenummer, Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummerAndErstellerLikeAndBezahlt(Integer nummer, Integer kundenummer, String ersteller,
            Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummerAndErstellerLike(Integer nummer, Integer kundenummer, String ersteller,
            Pageable pagination);

    Page<RechnungView> findByNummerAndErstellerLikeAndBezahlt(Integer nummer, String ersteller, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByNummerAndErstellerLike(Integer nummer, String ersteller, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummerAndBezahlt(Integer nummer, String reparaturnummer, Integer kundenummer,
            final Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummer(Integer nummer, String reparaturnummer, Integer kundenummer,
            Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummerAndErstellerLikeAndBezahlt(Integer nummer, String reparaturnummer,
            Integer kundenummer, String ersteller, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummerAndErstellerLike(Integer nummer, String reparaturnummer,
            Integer kundenummer, String ersteller, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndBezahlt(String reparaturnummer, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByReparaturNummer(String reparaturnummer, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummerAndBezahlt(String reparaturnummer, Integer kundenummer, Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummer(String reparaturnummer, Integer kundenummer, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndErstellerLikeAndBezahlt(String reparaturnummer, String ersteller, Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndErstellerLike(String reparaturnummer, String ersteller, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummerAndErstellerLikeAndBezahlt(String reparaturnummer, Integer kundenummer,
            String ersteller, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummerAndErstellerLike(String reparaturnummer, Integer kundenummer, String ersteller,
            Pageable pagination);

    Page<RechnungView> findByKundeNummerAndBezahlt(Integer kundenummer, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByKundeNummer(Integer kundenummer, Pageable pagination);

    Page<RechnungView> findByKundeNummerAndErstellerLikeAndBezahlt(Integer kundenummer, String ersteller, Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByKundeNummerAndErstellerLike(Integer kundenummer, String ersteller, Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndBezahlt(String ersteller, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByErstellerLike(String ersteller, Pageable pagination);

    Page<RechnungView> findByBezahlt(Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByNummerAndArt(Integer nummer, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndBezahltAndArt(Integer nummer, String reparaturnummer, final Boolean bezahlt,
            Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndArt(Integer nummer, String reparaturnummer, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndErstellerLikeAndBezahltAndArt(Integer nummer, String reparaturnummer,
            String ersteller, final Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndErstellerLikeAndArt(Integer nummer, String reparaturnummer, String ersteller,
            Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummerAndBezahltAndArt(Integer nummer, Integer kundenummer, Boolean bezahlt, Integer art,
            Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummerAndArt(Integer nummer, Integer kundenummer, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummerAndErstellerLikeAndBezahltAndArt(Integer nummer, Integer kundenummer, String ersteller,
            Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndKundeNummerAndErstellerLikeAndArt(Integer nummer, Integer kundenummer, String ersteller, Integer art,
            Pageable pagination);

    Page<RechnungView> findByNummerAndErstellerLikeAndBezahltAndArt(Integer nummer, String ersteller, Boolean bezahlt, Integer art,
            Pageable pagination);

    Page<RechnungView> findByNummerAndErstellerLikeAndArt(Integer nummer, String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummerAndBezahltAndArt(Integer nummer, String reparaturnummer,
            Integer kundenummer, final Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummerAndArt(Integer nummer, String reparaturnummer, Integer kundenummer,
            Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummerAndErstellerLikeAndBezahltAndArt(Integer nummer, String reparaturnummer,
            Integer kundenummer, String ersteller, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByNummerAndReparaturNummerAndKundeNummerAndErstellerLikeAndArt(Integer nummer, String reparaturnummer,
            Integer kundenummer, String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndBezahltAndArt(String reparaturnummer, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndArt(String reparaturnummer, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummerAndBezahltAndArt(String reparaturnummer, Integer kundenummer, Boolean bezahlt,
            Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummerAndArt(String reparaturnummer, Integer kundenummer, Integer art,
            Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndErstellerLikeAndBezahltAndArt(String reparaturnummer, String ersteller, Boolean bezahlt,
            Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndErstellerLikeAndArt(String reparaturnummer, String ersteller, Integer art,
            Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummerAndErstellerLikeAndBezahltAndArt(String reparaturnummer, Integer kundenummer,
            String ersteller, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNummerAndKundeNummerAndErstellerLikeAndArt(String reparaturnummer, Integer kundenummer,
            String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByKundeNummerAndBezahltAndArt(Integer kundenummer, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByKundeNummerAndArt(Integer kundenummer, Integer art, Pageable pagination);

    Page<RechnungView> findByKundeNummerAndErstellerLikeAndBezahltAndArt(Integer kundenummer, String ersteller, Boolean bezahlt,
            Integer art, Pageable pagination);

    Page<RechnungView> findByKundeNummerAndErstellerLikeAndArt(Integer kundenummer, String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndBezahltAndArt(String ersteller, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndArt(String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByBezahltAndArt(Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByMitAngebotAndMitReparatur(Boolean mitAngebot, Boolean mitReparatur, Pageable pagination);

    Page<RechnungView> findByMitReparatur(Boolean mitReparatur, Pageable pagination);

    Page<RechnungView> findByMitBestellungAndMitReparatur(Boolean mitBestellung, Boolean mitReparatur, Pageable pagination);

    Page<RechnungView> findByMitAngebotAndMitBestellungAndMitReparatur(Boolean mitAngebot, Boolean mitBestellung, Boolean mitReparatur,
            Pageable pagination);

    Page<RechnungView> findByMitBestellung(Boolean mitBestellung, Pageable pagination);

    Page<RechnungView> findByMitAngebotAndMitBestellung(Boolean mitAngebot, Boolean mitBestellung, Pageable pagination);

    Page<RechnungView> findByMitAngebot(Boolean mitAngebot, Pageable pagination);

    @Query(value = "SELECT * FROM vrechnung WHERE EXISTS ( SELECT 1 FROM rechnungsposten WHERE rechnungsposten.rechnung_id = vrechnung.id AND (bezeichnung LIKE :bezeichnung OR seriennummer LIKE :seriennummer OR hinweis LIKE :hinweis))", countQuery = "SELECT COUNT(*) FROM vrechnung WHERE EXISTS ( SELECT 1 FROM rechnungsposten WHERE rechnungsposten.rechnung_id = vrechnung.id AND (bezeichnung LIKE :bezeichnung OR seriennummer LIKE :seriennummer OR hinweis LIKE :hinweis))", nativeQuery = true)
    Page<RechnungView> findByPostenBezeichnungLikeOrPostenSeriennummerLikeOrPostenHinweisLike(@Param("bezeichnung") String bezeichnung,
            @Param("seriennummer") String seriennummer, @Param("hinweis") String hinweis, Pageable pagination);

}
