package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;

import de.sg.computerinsel.tools.CurrencyUtils;
import de.sg.computerinsel.tools.DateUtils;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.rechnung.rest.model.RechnungDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@UtilityClass
@Slf4j
public class KassenbuchStatistikUtils {

    private static final String TRENNZEICHEN = "|";

    public static Map<Integer, Map<Zahlart, Map<Month, List<RechnungDTO>>>> getStatistikProJahrZahlungsartMonat(
            final List<RechnungDTO> rechnungen) {
        final Map<Integer, Map<Zahlart, Map<Month, List<RechnungDTO>>>> result = new TreeMap<>();
        for (final Entry<LocalDate, List<RechnungDTO>> entry : getRechnungenJeJahr(rechnungen).entrySet()) {
            final int year = entry.getKey().getYear();
            result.put(year, getStatistikProZahlungsartMonat(entry.getValue()));
        }
        return result;
    }

    private static Map<LocalDate, List<RechnungDTO>> getRechnungenJeJahr(final List<RechnungDTO> rechnungen) {
        return rechnungen.stream().collect(Collectors.groupingBy(RechnungDTO::getRechnungsjahr));
    }

    public void createFile(final File ablageverzeichnis,
            final Map<Integer, Map<Zahlart, Map<Month, List<RechnungDTO>>>> rechnungProJahrMonatZahlungsart) throws IOException {
        for (final Entry<Integer, Map<Zahlart, Map<Month, List<RechnungDTO>>>> entry : rechnungProJahrMonatZahlungsart.entrySet()) {
            final File file = createFileZahlweg(ablageverzeichnis, entry.getKey());
            final FileWriter writer = new FileWriter(file.getAbsoluteFile());
            writeToFile(writer, entry.getValue());
            writer.flush();
            writer.close();
        }
    }

    private void writeToFile(final FileWriter writer, final Map<Zahlart, Map<Month, List<RechnungDTO>>> rechnungProZahlartMonat)
            throws IOException {
        writeHeadline(writer);
        writeLineJeZahlungsart(writer, rechnungProZahlartMonat);
        writeFootline(writer, rechnungProZahlartMonat);
    }

    private void writeHeadline(final FileWriter writer) throws IOException {
        writer.write(TRENNZEICHEN);
        for (final Month entry : Month.values()) {
            writer.write(entry.toString());
            writer.write(TRENNZEICHEN);
        }
        writer.write("\n\r");
    }

    private void writeLineJeZahlungsart(final FileWriter writer, final Map<Zahlart, Map<Month, List<RechnungDTO>>> rechnungProZahlartMonat)
            throws IOException {
        for (final Entry<Zahlart, Map<Month, List<RechnungDTO>>> entry : rechnungProZahlartMonat.entrySet()) {
            writer.write(entry.getKey().getBezeichnung());
            writer.write(TRENNZEICHEN);
            for (final Entry<Month, List<RechnungDTO>> entry2 : entry.getValue().entrySet()) {
                final BigDecimal betrag = entry2.getValue().stream().map(RechnungDTO::getRechnungsbetrag).reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);
                writer.write(CurrencyUtils.format(betrag));
                writer.write(TRENNZEICHEN);
            }
            writer.write("\n\r");
        }
        writer.write("\n\r");
    }

    private void writeFootline(final FileWriter writer, final Map<Zahlart, Map<Month, List<RechnungDTO>>> rechnungProZahlartMonat)
            throws IOException {
        writer.write("Gesamt");
        writer.write(TRENNZEICHEN);
        final Map<Month, BigDecimal> result = new TreeMap<>();
        for (final Entry<Zahlart, Map<Month, List<RechnungDTO>>> entry : rechnungProZahlartMonat.entrySet()) {
            for (final Entry<Month, List<RechnungDTO>> entry2 : entry.getValue().entrySet()) {
                BigDecimal betrag = result.get(entry2.getKey()) == null ? BigDecimal.ZERO : result.get(entry2.getKey());
                betrag = betrag.add(
                        entry2.getValue().stream().map(RechnungDTO::getRechnungsbetrag).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
                result.put(entry2.getKey(), betrag);
            }
        }
        for (final Entry<Month, BigDecimal> entry : result.entrySet()) {
            writer.write(CurrencyUtils.format(entry.getValue()));
            writer.write(TRENNZEICHEN);
        }
    }

    private File createFileZahlweg(final File ablageverzeichnis, final int jahr) {
        return createFile(ablageverzeichnis, jahr, "Statistik_Zahlweg_Jahr_");
    }

    private File createFilePosten(final File ablageverzeichnis, final int jahr) {
        return createFile(ablageverzeichnis, jahr, "Statistik_Posten_Jahr_");
    }

    private File createFile(final File ablageverzeichnis, final int jahr, final String name) {
        return new File(ablageverzeichnis, DateUtils.nowDatetime() + name + jahr + ".xls");
    }

    private static Map<Zahlart, Map<Month, List<RechnungDTO>>> getStatistikProZahlungsartMonat(final List<RechnungDTO> rechnungen) {
        final Map<Zahlart, List<RechnungDTO>> rechnungJeZahlart = getStatistikProZahlungsart(rechnungen);
        final Map<Zahlart, Map<Month, List<RechnungDTO>>> result = new TreeMap<>();
        for (final Entry<Zahlart, List<RechnungDTO>> entry : rechnungJeZahlart.entrySet()) {
            result.put(entry.getKey(), getStatistikProMonat(entry.getValue()));
        }
        for (final Zahlart zahlart : Zahlart.values()) {
            result.computeIfAbsent(zahlart, k -> getEmptyStatitsikProMonat());
        }
        return result;
    }

    private static Map<Month, List<RechnungDTO>> getStatistikProMonat(final List<RechnungDTO> rechnungen) {
        final Map<Month, List<RechnungDTO>> rechnungJeMonat = new TreeMap<>(
                rechnungen.stream().collect(Collectors.groupingBy(RechnungDTO::getRechnungsmonat)));
        for (final Month month : Month.values()) {
            rechnungJeMonat.computeIfAbsent(month, k -> new ArrayList<>());
        }
        return rechnungJeMonat;
    }

    private static Map<Month, List<RechnungDTO>> getEmptyStatitsikProMonat() {
        return Arrays.asList(Month.values()).stream().collect(Collectors.toMap(m -> m, m -> new ArrayList<>()));
    }

    private static Map<Zahlart, List<RechnungDTO>> getStatistikProZahlungsart(final List<RechnungDTO> rechnungen) {
        return rechnungen.stream().filter(r -> Objects.nonNull(r.getArt())).collect(Collectors.groupingBy(RechnungDTO::getArt));
    }

    public static Map<Integer, Map<String, Map<Month, BigDecimal>>> getStatistikProJahrPostenMonat(final List<RechnungDTO> rechnungen,
            final String postenfilter) {
        final Map<Integer, Map<String, Map<Month, BigDecimal>>> result = new TreeMap<>();
        for (final Entry<LocalDate, List<RechnungDTO>> entry : getRechnungenJeJahr(rechnungen).entrySet()) {
            final int year = entry.getKey().getYear();
            result.put(year, getStatistikProPostenMonat(entry.getValue(), postenfilter));
        }
        return result;
    }

    public static Map<Month, BigDecimal> getStatistikPostenProMonat(final List<RechnungDTO> rechnungen, final String postenfilter) {
        final Map<Month, BigDecimal> result = new TreeMap<>();
        for (final Entry<Month, List<RechnungDTO>> entry : getStatistikProMonat(rechnungen).entrySet()) {
            log.debug("Monat: {}", entry.getKey());
            final List<Rechnungsposten> list = entry.getValue().stream().filter(r -> Objects.nonNull(r.getPosten()))
                    .map(RechnungDTO::getPosten).flatMap(Collection::stream)
                    .filter(p -> StringUtils.containsIgnoreCase(p.getBezeichnung(), postenfilter)).collect(Collectors.toList());
            for (final Rechnungsposten posten : list) {
                log.debug("{} {}", posten.getBezeichnung(), CurrencyUtils.format(posten.getGesamt()));
            }
            final BigDecimal betrag = list.stream().map(Rechnungsposten::getGesamt).filter(Objects::nonNull).reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            result.put(entry.getKey(), betrag);
        }
        return result;
    }

    public void createPostenFile(final File ablageverzeichnis,
            final Map<Integer, Map<String, Map<Month, BigDecimal>>> statistikProJahrPostenMonat) throws IOException {
        for (final Entry<Integer, Map<String, Map<Month, BigDecimal>>> entry : statistikProJahrPostenMonat.entrySet()) {
            final File file = createFilePosten(ablageverzeichnis, entry.getKey());
            try (final FileWriter writer = new FileWriter(file.getAbsoluteFile())) {
                writePostenToFile(writer, entry.getValue(), getStatistikPostenProMonat(entry.getValue()));
                writer.flush();
            }
        }
    }

    public void createUeberweisungenUebersichtFile(final File ablageverzeichnis, final List<RechnungDTO> ueberweisungen,
            final String zeitraumVon, final String zeitraumBis) throws IOException {
        final File file = new File(ablageverzeichnis, DateUtils.nowDatetime() + "_uebersicht_ueberweisungen.xls");
        try (final FileWriter writer = new FileWriter(file.getAbsoluteFile())) {
            writer.write("Rechnungszeitraum von " + zeitraumVon + " (inkl.) bis " + zeitraumBis + " (inkl.)\n\r\n\r");
            writer.write("Rechnungsnummer|Name|Betrag|Rechnungsdatum\n\r");
            for (final RechnungDTO rechnung : ueberweisungen) {
                writeUeberweisungToFile(writer, rechnung);
            }
            writer.flush();
        }
    }

    private void writeUeberweisungToFile(final FileWriter writer, final RechnungDTO rechnung) throws IOException {
        writer.write(Joiner.on("|").join(rechnung.getRechnung().getNummerAnzeige(), rechnung.getKundenAdresse(),
                CurrencyUtils.format(rechnung.getRechnungsbetrag()), DateUtils.format(rechnung.getRechnung().getDatum())));
        writer.write("\n\r");
    }

    private Map<Month, BigDecimal> getStatistikPostenProMonat(
            final Map<String, Map<Month, BigDecimal>> statistikProBezeichnungMonatBetrag) {
        final Map<Month, BigDecimal> result = new TreeMap<>();
        for (final Month month : Month.values()) {
            result.put(month, BigDecimal.ZERO);
        }

        final List<Map<Month, BigDecimal>> list = statistikProBezeichnungMonatBetrag.entrySet().stream().map(Entry::getValue)
                .collect(Collectors.toList());
        for (final Map<Month, BigDecimal> map : list) {
            for (final Entry<Month, BigDecimal> entry : map.entrySet()) {
                final BigDecimal betrag = result.get(entry.getKey()).add(entry.getValue());
                result.put(entry.getKey(), betrag);
            }
        }
        return result;
    }

    private void writePostenToFile(final FileWriter writer, final Map<String, Map<Month, BigDecimal>> statistikProPostenMonat,
            final Map<Month, BigDecimal> statistikProMonat) throws IOException {
        writeHeadline(writer);
        writeLineJePosten(writer, statistikProPostenMonat);
        writePostenFootline(writer, statistikProMonat);
    }

    private void writeLineJePosten(final FileWriter writer, final Map<String, Map<Month, BigDecimal>> statistikProPostenMonat)
            throws IOException {
        for (final Entry<String, Map<Month, BigDecimal>> entry : statistikProPostenMonat.entrySet()) {
            writer.write(entry.getKey());
            writer.write(TRENNZEICHEN);
            for (final Entry<Month, BigDecimal> entry2 : entry.getValue().entrySet()) {
                writer.write(CurrencyUtils.format(entry2.getValue()));
                writer.write(TRENNZEICHEN);
            }
            writer.write("\n\r");
        }
        writer.write("\n\r");
    }

    private void writePostenFootline(final FileWriter writer, final Map<Month, BigDecimal> statistikProMonat) throws IOException {
        writer.write("Gesamt");
        writer.write(TRENNZEICHEN);
        for (final Entry<Month, BigDecimal> entry : statistikProMonat.entrySet()) {
            writer.write(CurrencyUtils.format(entry.getValue()));
            writer.write(TRENNZEICHEN);
        }
    }

    private static Map<String, Map<Month, BigDecimal>> getStatistikProPostenMonat(final List<RechnungDTO> rechnungen,
            final String postenfilter) {
        final Map<String, Map<Month, BigDecimal>> result = new TreeMap<>();
        for (final Entry<Month, List<RechnungDTO>> entry : getStatistikProMonat(rechnungen).entrySet()) {
            log.debug("Monat: {}", entry.getKey());
            for (final Entry<String, List<Rechnungsposten>> posten : getStatistikProPosten(entry.getValue(), postenfilter).entrySet()) {
                log.debug("{} {}", posten.getKey(), CurrencyUtils.format(getBetragPosten(posten.getValue())));
                Map<Month, BigDecimal> map = result.get(posten.getKey());
                if (map == null) {
                    map = new TreeMap<>();
                    for (final Month month : Month.values()) {
                        map.put(month, BigDecimal.ZERO);
                    }
                }
                final BigDecimal betrag = map.get(entry.getKey()) == null ? BigDecimal.ZERO : map.get(entry.getKey());
                map.put(entry.getKey(), betrag.add(getBetragPosten(posten.getValue())));
                result.put(posten.getKey(), map);
            }
        }
        return result;
    }

    private static BigDecimal getBetragPosten(final List<Rechnungsposten> posten) {
        return posten.stream().map(Rechnungsposten::getGesamt).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private static Map<String, List<Rechnungsposten>> getStatistikProPosten(final List<RechnungDTO> rechnungen, final String postenfilter) {
        return rechnungen.stream().filter(r -> Objects.nonNull(r.getPosten())).map(RechnungDTO::getPosten).flatMap(Collection::stream)
                .filter(p -> StringUtils.containsIgnoreCase(p.getBezeichnung(), postenfilter))
                .collect(Collectors.groupingBy(Rechnungsposten::getBezeichnung));
    }

}
