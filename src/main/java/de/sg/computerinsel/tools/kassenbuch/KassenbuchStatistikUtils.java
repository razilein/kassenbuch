package de.sg.computerinsel.tools.kassenbuch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import de.sg.computerinsel.tools.kassenbuch.model.Zahlart;
import lombok.experimental.UtilityClass;

/**
 * @author Sita Geßner
 */

@UtilityClass
public class KassenbuchStatistikUtils {

    public static Map<Integer, Map<Zahlart, Map<Month, List<Rechnung>>>> getStatistikProJahrZahlungsartMonat(
            final List<Rechnung> rechnungen) {
        final Map<Date, List<Rechnung>> rechnungJeJahr = rechnungen.stream().collect(Collectors.groupingBy(Rechnung::getRechnungsjahr));
        final Map<Integer, Map<Zahlart, Map<Month, List<Rechnung>>>> result = new TreeMap<>();
        for (final Entry<Date, List<Rechnung>> entry : rechnungJeJahr.entrySet()) {
            final int year = entry.getKey().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            result.put(year, getStatistikProZahlungsartMonat(entry.getValue()));
        }
        return result;
    }

    public void createFile(final File ablageverzeichnis,
            final Map<Integer, Map<Zahlart, Map<Month, List<Rechnung>>>> rechnungProJahrMonatZahlungsart) throws IOException {
        for (final Entry<Integer, Map<Zahlart, Map<Month, List<Rechnung>>>> entry : rechnungProJahrMonatZahlungsart.entrySet()) {
            final File file = createFile(ablageverzeichnis, entry.getKey());
            final FileWriter writer = new FileWriter(file.getAbsoluteFile());
            writeToFile(writer, entry.getValue());
            writer.flush();
            writer.close();
        }
    }

    private void writeToFile(final FileWriter writer, final Map<Zahlart, Map<Month, List<Rechnung>>> rechnungProZahlartMonat)
            throws IOException {
        writeHeadline(writer);
        writeLineJeZahlungsart(writer, rechnungProZahlartMonat);
        writeFootline(writer, rechnungProZahlartMonat);
    }

    private void writeHeadline(final FileWriter writer) throws IOException {
        writer.write(";");
        for (final Month entry : Month.values()) {
            writer.write(entry.toString());
            writer.write(";");
        }
        writer.write("\n\r");
    }

    private void writeLineJeZahlungsart(final FileWriter writer, final Map<Zahlart, Map<Month, List<Rechnung>>> rechnungProZahlartMonat)
            throws IOException {
        for (final Entry<Zahlart, Map<Month, List<Rechnung>>> entry : rechnungProZahlartMonat.entrySet()) {
            writer.write(entry.getKey().getBezeichnung());
            writer.write(";");
            for (final Entry<Month, List<Rechnung>> entry2 : entry.getValue().entrySet()) {
                final BigDecimal betrag = entry2.getValue().stream().map(Rechnung::getRechnungsbetrag).reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);
                writer.write(KassenbuchErstellenUtils.BETRAG_FORMAT.format(betrag));
                writer.write(";");
            }
            writer.write("\n\r");
        }
        writer.write("\n\r");

    }

    private void writeFootline(final FileWriter writer, final Map<Zahlart, Map<Month, List<Rechnung>>> rechnungProZahlartMonat)
            throws IOException {
        writer.write("Gesamt");
        writer.write(";");
        final Map<Month, BigDecimal> result = new TreeMap<>();
        for (final Entry<Zahlart, Map<Month, List<Rechnung>>> entry : rechnungProZahlartMonat.entrySet()) {
            for (final Entry<Month, List<Rechnung>> entry2 : entry.getValue().entrySet()) {
                BigDecimal betrag = result.get(entry2.getKey()) == null ? BigDecimal.ZERO : result.get(entry2.getKey());
                betrag = betrag
                        .add(entry2.getValue().stream().map(Rechnung::getRechnungsbetrag).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
                result.put(entry2.getKey(), betrag);
            }
        }
        for (final Entry<Month, BigDecimal> entry : result.entrySet()) {
            writer.write(KassenbuchErstellenUtils.BETRAG_FORMAT.format(entry.getValue()));
            writer.write(";");
        }
    }

    private File createFile(final File ablageverzeichnis, final int jahr) {
        return new File(ablageverzeichnis,
                KassenbuchErstellenUtils.DATE_FORMAT_FILES.format(new Date()) + "Statistik_Jahr_" + jahr + ".xls");
    }

    private static Map<Zahlart, Map<Month, List<Rechnung>>> getStatistikProZahlungsartMonat(final List<Rechnung> rechnungen) {
        final Map<Zahlart, List<Rechnung>> rechnungJeZahlart = getStatistikProZahlungsart(rechnungen);
        final Map<Zahlart, Map<Month, List<Rechnung>>> result = new TreeMap<>();
        for (final Entry<Zahlart, List<Rechnung>> entry : rechnungJeZahlart.entrySet()) {
            result.put(entry.getKey(), getStatistikProMonat(entry.getValue()));
        }
        for (final Zahlart zahlart : Zahlart.values()) {
            if (result.get(zahlart) == null) {
                result.put(zahlart, getEmptyStatitsikProMonat());
            }
        }
        return result;
    }

    private static Map<Month, List<Rechnung>> getStatistikProMonat(final List<Rechnung> rechnungen) {
        final Map<Month, List<Rechnung>> rechnungJeMonat = new TreeMap<>(
                rechnungen.stream().collect(Collectors.groupingBy(Rechnung::getRechnungsmonat)));
        for (final Month month : Month.values()) {
            if (rechnungJeMonat.get(month) == null) {
                rechnungJeMonat.put(month, new ArrayList<>());
            }
        }
        return rechnungJeMonat;
    }

    private static Map<Month, List<Rechnung>> getEmptyStatitsikProMonat() {
        return Arrays.asList(Month.values()).stream().collect(Collectors.toMap(m -> m, m -> new ArrayList<>()));
    }

    private static Map<Zahlart, List<Rechnung>> getStatistikProZahlungsart(final List<Rechnung> rechnungen) {
        return rechnungen.stream().filter(r -> Objects.nonNull(r.getArt())).collect(Collectors.groupingBy(Rechnung::getArt));
    }
}
