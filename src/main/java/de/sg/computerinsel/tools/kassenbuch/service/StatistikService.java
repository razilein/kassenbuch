package de.sg.computerinsel.tools.kassenbuch.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.kassenbuch.KassenbuchErstellenUtils;
import de.sg.computerinsel.tools.kassenbuch.KassenbuchStatistikUtils;
import de.sg.computerinsel.tools.kassenbuch.RechnungenEinlesenUtils;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import de.sg.computerinsel.tools.kassenbuch.model.Zahlart;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchStatistik;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Service
@Slf4j
@AllArgsConstructor
public class StatistikService {

    private final EinstellungenService einstellungenService;

    public void erstellenZahlungsarten(final KassenbuchStatistik statistik) {
        final List<Rechnung> rechnungen = RechnungenEinlesenUtils.readHtmlFilesWithPosten(
                new File(einstellungenService.getRechnungsverzeichnis().getWert()), statistik.getZeitraumVon(), statistik.getZeitraumBis());
        log.debug("{} Rechnungen ausgelesen", rechnungen.size());
        final File ablageverzeichnis = new File(einstellungenService.getAblageverzeichnis().getWert());
        try {
            final Map<Integer, Map<Zahlart, Map<Month, List<Rechnung>>>> statistikProJahrZahlungsartMonat = KassenbuchStatistikUtils
                    .getStatistikProJahrZahlungsartMonat(rechnungen);
            KassenbuchStatistikUtils.createFile(ablageverzeichnis, statistikProJahrZahlungsartMonat);

            final Map<Integer, Map<String, Map<Month, BigDecimal>>> statistikProJahrPostenMonat = KassenbuchStatistikUtils
                    .getStatistikProJahrPostenMonat(rechnungen, statistik.getPosten());
            KassenbuchStatistikUtils.createPostenFile(ablageverzeichnis, statistikProJahrPostenMonat);
        } catch (final IOException e) {
            throw new IllegalArgumentException("Fehler beim Erzeugen der Statistik. Näheres ist der Log-Datei zu entnehmen.", e);
        }
    }

    public void erstellenUeberweisungen(final KassenbuchStatistik statistik) {
        final List<Rechnung> ueberweisungen = RechnungenEinlesenUtils
                .readHtmlFilesWithPosten(new File(einstellungenService.getRechnungsverzeichnis().getWert()), statistik.getZeitraumVon(),
                        statistik.getZeitraumBis())
                .stream().filter(u -> Zahlart.UEBERWEISUNG == u.getArt()).collect(Collectors.toList());
        Collections.sort(ueberweisungen, KassenbuchErstellenUtils.rechnungComparator());
        log.debug("{} Rechnungen ausgelesen", ueberweisungen.size());
        final File ablageverzeichnis = new File(einstellungenService.getAblageverzeichnis().getWert());
        try {
            final String zeitraumVon = DateUtils.format(statistik.getZeitraumVon());
            final String zeitraumBis = DateUtils.format(statistik.getZeitraumBis());
            KassenbuchStatistikUtils.createUeberweisungenUebersichtFile(ablageverzeichnis, ueberweisungen, zeitraumVon, zeitraumBis);
        } catch (final IOException e) {
            throw new IllegalArgumentException("Fehler beim Erzeugen der Statistik. Näheres ist der Log-Datei zu entnehmen.", e);
        }
    }

}
