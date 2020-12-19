package de.sg.computerinsel.tools.rechnung.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.Einstellungen;
import de.sg.computerinsel.tools.kassenbuch.dao.KassenbuchpostenRepository;
import de.sg.computerinsel.tools.kassenbuch.model.Kassenbuchposten;
import de.sg.computerinsel.tools.rechnung.dao.RechnungRepository;
import de.sg.computerinsel.tools.rechnung.model.CsvExport;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungExportDto;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.FilialeKonten;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class RechnungExportService {

    private static final String CSV_HEADER = "\"Belegdatum\";\"Buchungsdatum\";\"Belegnummernkreis\";\"Belegnummer\";\"Buchungstext\";\"Buchungsbetrag\";\"Sollkonto\";\"Habenkonto\";\"Steuerschlüssel\";\"Kostenstelle 1\";\"Kostenstelle 2\";\"Buchungsbetrag Euro\";\"Waehrung\"";

    private final EinstellungenService einstellungenService;

    private final KontoService kontoService;

    private final KassenbuchpostenRepository kassenbuchpostenRepository;

    private final RechnungRepository rechnungRepository;

    private final RechnungService rechnungService;

    public int export(final RechnungExportDto dto) throws IOException {
        final List<Rechnung> rechnungen = listRechnungInZeitraum(dto);
        exportRechnungen(rechnungen);
        exportKassenbuch(dto, rechnungen);
        return rechnungen.size();
    }

    private void exportRechnungen(final List<Rechnung> rechnungenInZeitraum) throws IOException {
        final List<RechnungDTO> rechnungen = rechnungenInZeitraum.stream()
                .map(r -> new RechnungDTO(r, rechnungService.listRechnungspostenByRechnungId(r.getId()), false))
                .collect(Collectors.toList());
        exportToFile(rechnungen, kontoService.getKontenJeFiliale());
    }

    private void exportKassenbuch(final RechnungExportDto dto, final List<Rechnung> rechnungen) throws IOException {
        final Set<String> rechnungsnummern = rechnungen.stream().map(r -> r.getFiliale().getKuerzel() + r.getNummerAnzeige())
                .collect(Collectors.toSet());
        final List<String> lines = new ArrayList<>();
        lines.add(CSV_HEADER);
        final List<Kassenbuchposten> posten = listKassenbuchInZeitraum(dto).stream()
                .filter(p -> !rechnungsnummern.contains(p.getVerwendungszweck())).collect(Collectors.toList());
        posten.stream().map(CsvExport::new).forEach(csv -> lines.add(csv.toCsvString()));

        FileUtils.writeLines(createFileKassenbuch(), StandardCharsets.ISO_8859_1.name(), lines);
    }

    private void exportToFile(final List<RechnungDTO> rechnungen, final Map<Filiale, FilialeKonten> kontenJeFiliale) throws IOException {
        final List<String> lines = new ArrayList<>();
        lines.add(CSV_HEADER);

        BigDecimal umsatzNetto = BigDecimal.ZERO;
        BigDecimal umsatzBrutto = BigDecimal.ZERO;
        for (final RechnungDTO rechnung : rechnungen) {
            final FilialeKonten konten = kontenJeFiliale.get(rechnung.getRechnung().getFiliale());
            lines.add(createRechnung(rechnung, konten).toCsvString());

            umsatzNetto = umsatzNetto.add(rechnung.getNettobetrag());
            umsatzBrutto = umsatzBrutto.add(rechnung.getRechnungsbetrag());
        }
        log.info("Umsatz Netto: {}", umsatzNetto);
        log.info("Umsatz Brutto: {}", umsatzBrutto);
        FileUtils.writeLines(createFile(), StandardCharsets.ISO_8859_1.name(), lines);
    }

    private CsvExport createRechnung(final RechnungDTO rechnung, final FilialeKonten konten) {
        final CsvExport export = new CsvExport(rechnung);
        export.setHabenkonto(kontoService.getRechnungHaben(rechnung.getArt(), konten));
        export.setSollkonto(kontoService.getRechnungSoll(rechnung.getArt(), konten));
        return export;
    }

    private File createFile() {
        final Einstellungen ablageverzeichnis = einstellungenService.getAblageverzeichnis();
        return new File(ablageverzeichnis.getWert(), DateUtils.nowDatetime().concat("_").concat("rechnungen_").concat(".txt"));
    }

    private File createFileKassenbuch() {
        final Einstellungen ablageverzeichnis = einstellungenService.getAblageverzeichnis();
        return new File(ablageverzeichnis.getWert(), DateUtils.nowDatetime().concat("_").concat("kassenbuch_").concat(".txt"));
    }

    public List<Rechnung> listRechnungInZeitraum(final RechnungExportDto dto) {
        final LocalDate datumVon = LocalDate.of(dto.getJahr(), dto.getMonat(), 1);
        final LocalDate datumBis = LocalDate.of(dto.getJahr(), dto.getMonat(), 1).plusMonths(1).minusDays(1);
        return rechnungRepository.findAllByDatumGreaterThanEqualAndDatumLessThanEqualOrderByNummerAsc(datumVon, datumBis);
    }

    public List<Kassenbuchposten> listKassenbuchInZeitraum(final RechnungExportDto dto) {
        final LocalDate datumVon = LocalDate.of(dto.getJahr(), dto.getMonat(), 1);
        final LocalDate datumBis = LocalDate.of(dto.getJahr(), dto.getMonat(), 1).plusMonths(1).minusDays(1);
        return kassenbuchpostenRepository
                .findAllByKassenbuchDatumGreaterThanEqualAndKassenbuchDatumLessThanEqualAndKassenbuchGeloescht(datumVon, datumBis, false);
    }

}
