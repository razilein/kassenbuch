package de.sg.computerinsel.tools.kassenbuch.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.Einstellungen;
import de.sg.computerinsel.tools.kassenbuch.KassenbuchBearbeitenUtils;
import de.sg.computerinsel.tools.kassenbuch.KassenbuchErstellenUtils;
import de.sg.computerinsel.tools.kassenbuch.KassenstandBerechnenUtils;
import de.sg.computerinsel.tools.kassenbuch.RechnungenEinlesenUtils;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchEintragungManuell;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchErstellenData;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class KassenbuchErstellenService {

    private final EinstellungenService einstellungenService;

    public Map<String, Object> startRechnungsablage(final String rechnungsPath, final String ablagePath,
            final KassenbuchErstellenData data) {
        final Map<String, Object> result = new HashMap<>();
        File csvFile = null;
        File pdfFile = null;
        final List<Rechnung> rechnungen = RechnungenEinlesenUtils.readHtmlFiles(new File(rechnungsPath), data.getZeitraumVon(),
                data.getZeitraumBis());
        if (rechnungen.isEmpty()) {
            log.info("Keine Rechnungen gefunden.");
            result.put(Message.INFO.getCode(),
                    "Im hinterlegten Rechnungsverzeichnis konnten keine BAR-Rechnungen im angegebenen Zeitraum vom "
                            + DateUtils.format(data.getZeitraumVon()) + " bis " + DateUtils.format(data.getZeitraumBis())
                            + " gefunden werden.");
        } else {
            final Rechnung ausgangsRechnung = createStartBetrag(data.getAusgangsbetrag(), data.getAusgangsbetragDatum());
            csvFile = KassenbuchErstellenUtils.createCsv(rechnungen, ausgangsRechnung, ablagePath);
            pdfFile = KassenbuchErstellenUtils.createPdf(rechnungen, ausgangsRechnung, ablagePath);
            updateSettings(csvFile.getAbsolutePath(), pdfFile.getAbsolutePath(), ablagePath);
            log.info("Kassenbuch-Erstellung beendet.");

            if (pdfFile.exists() && csvFile.exists()) {
                result.put(Message.SUCCESS.getCode(),
                        "Das Kassenbuch wurde erfolgreich erstellt und unter dem Dateinamen: \r\n'" + pdfFile.getName() + "' ablegt.");
            } else {
                result.put(Message.ERROR.getCode(),
                        "Fehler beim Erstellen der Kassenbuch-Dateien. Siehe system.logs für weitere Hinweise.");
            }
        }
        return result;
    }

    public void manuelleEintragung(final List<KassenbuchEintragungManuell> eintragungen, final String csvDatei) {
        log.info("Kassenbuch-Bearbeitung gestartet.");
        String csvDateiPfad = csvDatei;
        for (final KassenbuchEintragungManuell eintragung : eintragungen) {
            final Rechnung neuerEintrag = KassenbuchBearbeitenUtils.createNeueEintragung(eintragung.getVerwendungszweck(),
                    eintragung.getDatum(), eintragung.getBetrag(),
                    KassenbuchEintragungManuell.IST_NEGATIVE_EINTRAGUNGSART.equals(eintragung.getEintragungsart()));
            final List<File> files = KassenbuchBearbeitenUtils.addKassenbuchEintrag(csvDateiPfad, neuerEintrag);
            updateSettings(files.get(0).getAbsolutePath(), files.get(1).getAbsolutePath(),
                    einstellungenService.getAblageverzeichnis().getWert());
            csvDateiPfad = files.get(0).getAbsolutePath();
        }
        log.info("Kassenbuch-Bearbeitung beendet.");
    }

    private void updateSettings(final String csvFilePath, final String pdfFilePath, final String ablagePath) {
        final Einstellungen letzteCsvDateipfad = einstellungenService.getLetzteCsvDateiPfad();
        letzteCsvDateipfad.setWert(csvFilePath);
        einstellungenService.save(letzteCsvDateipfad);

        final Einstellungen letztePdfDateipfad = einstellungenService.getLetztePdfDateiPfad();
        letztePdfDateipfad.setWert(pdfFilePath);
        einstellungenService.save(letztePdfDateipfad);

        final Einstellungen ausgangsbetrag = einstellungenService.getAusgangsbetrag();
        ausgangsbetrag.setWert(KassenstandBerechnenUtils.getGesamtbetragKassenbuch(csvFilePath, ablagePath));
        einstellungenService.save(ausgangsbetrag);
    }

    private Rechnung createStartBetrag(final BigDecimal startBetrag, final Date startBetragdatum) {
        final Rechnung rechnung = new Rechnung();
        rechnung.setRechnungsdatum(startBetragdatum);
        rechnung.setRechnungsbetrag(startBetrag);
        rechnung.setRechnungsnummer(Rechnung.AUSGANGSBETRAG);
        return rechnung;
    }

}
