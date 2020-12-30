package de.sg.computerinsel.tools.kassenbuch.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.kassenbuch.KassenbuchStatistikUtils;
import de.sg.computerinsel.tools.kassenbuch.rest.model.KassenbuchStatistik;
import de.sg.computerinsel.tools.rechnung.dao.RechnungRepository;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MessageService;
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

    private final MessageService messageService;

    private final RechnungRepository rechnungRepository;

    private final RechnungService rechnungService;

    public void erstellenZahlungsarten(final KassenbuchStatistik statistik) {
        final List<RechnungDTO> rechnungen = rechnungRepository
                .findAllByDatumGreaterThanEqualAndDatumLessThanEqual(DateUtils.convert(statistik.getZeitraumVon()),
                        DateUtils.convert(statistik.getZeitraumBis()))
                .stream().map(r -> rechnungService.getRechnung(r.getId())).collect(Collectors.toList());
        log.debug("{} Rechnungen ausgelesen", rechnungen.size());
        final File ablageverzeichnis = new File(einstellungenService.getAblageverzeichnis().getWert());
        try {
            final Map<Integer, Map<Zahlart, Map<Month, List<RechnungDTO>>>> statistikProJahrZahlungsartMonat = KassenbuchStatistikUtils
                    .getStatistikProJahrZahlungsartMonat(rechnungen);
            KassenbuchStatistikUtils.createFile(ablageverzeichnis, statistikProJahrZahlungsartMonat);

            final Map<Integer, Map<String, Map<Month, BigDecimal>>> statistikProJahrPostenMonat = KassenbuchStatistikUtils
                    .getStatistikProJahrPostenMonat(rechnungen, statistik.getPosten());
            KassenbuchStatistikUtils.createPostenFile(ablageverzeichnis, statistikProJahrPostenMonat);
        } catch (final IOException e) {
            throw new IllegalArgumentException(messageService.get("kassenbuch.statistik.error"), e);
        }
    }

    public void erstellenUeberweisungen(final KassenbuchStatistik statistik) {
        final List<RechnungDTO> ueberweisungen = rechnungRepository
                .findAllByDatumGreaterThanEqualAndDatumLessThanEqualAndArtAndVorlageOrderByDatumAscNummerAsc(
                        DateUtils.convert(statistik.getZeitraumVon()), DateUtils.convert(statistik.getZeitraumBis()),
                        Zahlart.UEBERWEISUNG.getCode(), false)
                .stream().map(r -> rechnungService.getRechnung(r.getId())).collect(Collectors.toList());
        log.debug("{} Rechnungen ausgelesen", ueberweisungen.size());
        final File ablageverzeichnis = new File(einstellungenService.getAblageverzeichnis().getWert());
        try {
            final String zeitraumVon = DateUtils.format(statistik.getZeitraumVon());
            final String zeitraumBis = DateUtils.format(statistik.getZeitraumBis());
            KassenbuchStatistikUtils.createUeberweisungenUebersichtFile(ablageverzeichnis, ueberweisungen, zeitraumVon, zeitraumBis);
        } catch (final IOException e) {
            throw new IllegalArgumentException(messageService.get("kassenbuch.statistik.error"), e);
        }
    }

}
