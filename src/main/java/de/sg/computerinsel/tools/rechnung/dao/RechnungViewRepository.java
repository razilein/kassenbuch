package de.sg.computerinsel.tools.rechnung.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.rechnung.model.RechnungView;

public interface RechnungViewRepository extends CrudRepository<RechnungView, Integer> {

    Page<RechnungView> findAll(Pageable pagination);

    Page<RechnungView> findByKundeId(Integer id, Pageable pagination);

    Page<RechnungView> findByArt(Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLike(String rechnungNr, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndBezahlt(String rechnungNr, String reparaturNr, final Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLike(String rechnungNr, String reparaturNr, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLikeAndBezahlt(String rechnungNr, String reparaturNr,
            String ersteller, final Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLike(String rechnungNr, String reparaturNr, String ersteller,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndBezahlt(String rechnungNr, String kundeNr, Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLike(String rechnungNr, String kundeNr, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLikeAndBezahlt(String rechnungNr, String kundeNr, String ersteller,
            Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLike(String rechnungNr, String kundeNr, String ersteller,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLikeAndBezahlt(String rechnungNr, String ersteller, Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLike(String rechnungNr, String ersteller, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndBezahlt(String rechnungNr, String reparaturNr, String kundeNr,
            final Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLike(String rechnungNr, String reparaturNr, String kundeNr,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahlt(String rechnungNr, String reparaturNr,
            String kundeNr, String ersteller, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLike(String rechnungNr, String reparaturNr,
            String kundeNr, String ersteller, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndBezahlt(String reparaturNr, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByReparaturNrLike(String reparaturNr, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndBezahlt(String reparaturNr, String kundeNr, Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLike(String reparaturNr, String kundeNr, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLikeAndBezahlt(String reparaturNr, String ersteller, Boolean bezahlt,
            Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLike(String reparaturNr, String ersteller, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahlt(String reparaturNr, String kundeNr, String ersteller,
            Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLike(String reparaturNr, String kundeNr, String ersteller,
            Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndBezahlt(String kundeNr, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByKundeNrLike(String kundeNr, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLikeAndBezahlt(String kundeNr, String ersteller, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLike(String kundeNr, String ersteller, Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndBezahlt(String ersteller, Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByErstellerLike(String ersteller, Pageable pagination);

    Page<RechnungView> findByBezahlt(Boolean bezahlt, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndArt(String rechnungNr, Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndBezahltAndArt(String rechnungNr, String reparaturNr, final Boolean bezahlt,
            Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndArt(String rechnungNr, String reparaturNr, Integer art,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLikeAndBezahltAndArt(String rechnungNr, String reparaturNr,
            String ersteller, final Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLikeAndArt(String rechnungNr, String reparaturNr, String ersteller,
            Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndBezahltAndArt(String rechnungNr, String kundeNr, Boolean bezahlt, Integer art,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndArt(String rechnungNr, String kundeNr, Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndArt(String rechnungNr, String kundeNr,
            String ersteller, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLikeAndArt(String rechnungNr, String kundeNr, String ersteller,
            Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLikeAndBezahltAndArt(String rechnungNr, String ersteller, Boolean bezahlt,
            Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLikeAndArt(String rechnungNr, String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndBezahltAndArt(String rechnungNr, String reparaturNr,
            String kundeNr, final Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndArt(String rechnungNr, String reparaturNr, String kundeNr,
            Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndArt(String rechnungNr,
            String reparaturNr, String kundeNr, String ersteller, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndArt(String rechnungNr, String reparaturNr,
            String kundeNr, String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndBezahltAndArt(String reparaturNr, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndArt(String reparaturNr, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndBezahltAndArt(String reparaturNr, String kundeNr, Boolean bezahlt, Integer art,
            Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndArt(String reparaturNr, String kundeNr, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLikeAndBezahltAndArt(String reparaturNr, String ersteller, Boolean bezahlt,
            Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLikeAndArt(String reparaturNr, String ersteller, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndArt(String reparaturNr, String kundeNr,
            String ersteller, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndArt(String reparaturNr, String kundeNr, String ersteller,
            Integer art, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndBezahltAndArt(String kundeNr, Boolean bezahlt, Integer art, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndArt(String kundeNr, Integer art, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLikeAndBezahltAndArt(String kundeNr, String ersteller, Boolean bezahlt, Integer art,
            Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLikeAndArt(String kundeNr, String ersteller, Integer art, Pageable pagination);

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

    Page<RechnungView> findByPostenBezeichnungLikeOrPostenSeriennummerLikeOrPostenHinweisLike(String bezeichnung, String seriennummer,
            String hinweis, Pageable pagination);

}
