package de.sg.computerinsel.tools.rechnung.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.sg.computerinsel.tools.rechnung.model.RechnungView;

public interface RechnungViewRepository extends CrudRepository<RechnungView, Integer> {

    Page<RechnungView> findByVorlage(Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeIdAndVorlage(Integer id, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByArtAndVorlage(Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndVorlage(String rechnungNr, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndBezahltAndVorlage(String rechnungNr, String reparaturNr,
            final Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndVorlage(String rechnungNr, String reparaturNr, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLikeAndBezahltAndVorlage(String rechnungNr, String reparaturNr,
            String ersteller, final Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLikeAndVorlage(String rechnungNr, String reparaturNr,
            String ersteller, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndBezahltAndVorlage(String rechnungNr, String kundeNr, Boolean bezahlt,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndVorlage(String rechnungNr, String kundeNr, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndVorlage(String rechnungNr, String kundeNr,
            String ersteller, Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLikeAndVorlage(String rechnungNr, String kundeNr, String ersteller,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLikeAndBezahltAndVorlage(String rechnungNr, String ersteller, Boolean bezahlt,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLikeAndVorlage(String rechnungNr, String ersteller, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndBezahltAndVorlage(String rechnungNr, String reparaturNr,
            String kundeNr, final Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndVorlage(String rechnungNr, String reparaturNr, String kundeNr,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndVorlage(String rechnungNr,
            String reparaturNr, String kundeNr, String ersteller, Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndVorlage(String rechnungNr, String reparaturNr,
            String kundeNr, String ersteller, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndBezahltAndVorlage(String reparaturNr, Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndVorlage(String reparaturNr, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndBezahltAndVorlage(String reparaturNr, String kundeNr, Boolean bezahlt,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndVorlage(String reparaturNr, String kundeNr, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLikeAndBezahltAndVorlage(String reparaturNr, String ersteller, Boolean bezahlt,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLikeAndVorlage(String reparaturNr, String ersteller, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndVorlage(String reparaturNr, String kundeNr,
            String ersteller, Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndVorlage(String reparaturNr, String kundeNr, String ersteller,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndBezahltAndVorlage(String kundeNr, Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndVorlage(String kundeNr, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLikeAndBezahltAndVorlage(String kundeNr, String ersteller, Boolean bezahlt,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLikeAndVorlage(String kundeNr, String ersteller, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndBezahltAndVorlage(String ersteller, Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndVorlage(String ersteller, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByBezahltAndVorlage(Boolean bezahlt, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndArtAndVorlage(String rechnungNr, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndBezahltAndArt(String rechnungNr, String reparaturNr, final Boolean bezahlt,
            Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndArtAndVorlage(String rechnungNr, String reparaturNr, Integer art,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLikeAndBezahltAndArtAndVorlage(String rechnungNr,
            String reparaturNr, String ersteller, final Boolean bezahlt, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndErstellerLikeAndArtAndVorlage(String rechnungNr, String reparaturNr,
            String ersteller, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndBezahltAndArtAndVorlage(String rechnungNr, String kundeNr, Boolean bezahlt,
            Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndArtAndVorlage(String rechnungNr, String kundeNr, Integer art, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndArtAndVorlage(String rechnungNr, String kundeNr,
            String ersteller, Boolean bezahlt, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndKundeNrLikeAndErstellerLikeAndArtAndVorlage(String rechnungNr, String kundeNr,
            String ersteller, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLikeAndBezahltAndArtAndVorlage(String rechnungNr, String ersteller, Boolean bezahlt,
            Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndErstellerLikeAndArtAndVorlage(String rechnungNr, String ersteller, Integer art,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndBezahltAndArtAndVorlage(String rechnungNr, String reparaturNr,
            String kundeNr, final Boolean bezahlt, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndArtAndVorlage(String rechnungNr, String reparaturNr,
            String kundeNr, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndArtAndVorlage(String rechnungNr,
            String reparaturNr, String kundeNr, String ersteller, Boolean bezahlt, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByRechnungNrLikeAndReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndArtAndVorlage(String rechnungNr,
            String reparaturNr, String kundeNr, String ersteller, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndBezahltAndArtAndVorlage(String reparaturNr, Boolean bezahlt, Integer art, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndArtAndVorlage(String reparaturNr, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndBezahltAndArtAndVorlage(String reparaturNr, String kundeNr, Boolean bezahlt,
            Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndArtAndVorlage(String reparaturNr, String kundeNr, Integer art, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLikeAndBezahltAndArtAndVorlage(String reparaturNr, String ersteller,
            Boolean bezahlt, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndErstellerLikeAndArtAndVorlage(String reparaturNr, String ersteller, Integer art,
            Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndBezahltAndArtAndVorlage(String reparaturNr, String kundeNr,
            String ersteller, Boolean bezahlt, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByReparaturNrLikeAndKundeNrLikeAndErstellerLikeAndArtAndVorlage(String reparaturNr, String kundeNr,
            String ersteller, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndBezahltAndArtAndVorlage(String kundeNr, Boolean bezahlt, Integer art, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndArtAndVorlage(String kundeNr, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLikeAndBezahltAndArtAndVorlage(String kundeNr, String ersteller, Boolean bezahlt,
            Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByKundeNrLikeAndErstellerLikeAndArtAndVorlage(String kundeNr, String ersteller, Integer art, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndBezahltAndArtAndVorlage(String ersteller, Boolean bezahlt, Integer art, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByErstellerLikeAndArtAndVorlage(String ersteller, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByBezahltAndArtAndVorlage(Boolean bezahlt, Integer art, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByMitAngebotAndMitReparaturAndVorlage(Boolean mitAngebot, Boolean mitReparatur, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByMitReparaturAndVorlage(Boolean mitReparatur, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByMitBestellungAndMitReparaturAndVorlage(Boolean mitBestellung, Boolean mitReparatur, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByMitAngebotAndMitBestellungAndMitReparaturAndVorlage(Boolean mitAngebot, Boolean mitBestellung,
            Boolean mitReparatur, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByMitBestellungAndVorlage(Boolean mitBestellung, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByMitAngebotAndMitBestellungAndVorlage(Boolean mitAngebot, Boolean mitBestellung, Boolean vorlage,
            Pageable pagination);

    Page<RechnungView> findByMitAngebotAndVorlage(Boolean mitAngebot, Boolean vorlage, Pageable pagination);

    Page<RechnungView> findByPostenBezeichnungLikeOrPostenSeriennummerLikeOrPostenHinweisLikeAndVorlage(String bezeichnung,
            String seriennummer, String hinweis, Boolean vorlage, Pageable pagination);

}
