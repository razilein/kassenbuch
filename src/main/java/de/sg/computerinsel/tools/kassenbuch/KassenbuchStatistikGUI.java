package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;
import de.sg.computerinsel.tools.kassenbuch.model.Rechnung;
import de.sg.computerinsel.tools.kassenbuch.model.Zahlart;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sita Geßner
 */
@Slf4j
public class KassenbuchStatistikGUI {

    private final JFrame main;
    private final Einstellungen einstellungen;

    private final JTextField posten = new JTextField(50);

    private final JTextField zeitraumVon = new JTextField(10);

    private final JTextField zeitraumBis = new JTextField(10);

    public KassenbuchStatistikGUI(final JFrame main, final Einstellungen einstellungen) {
        this.main = main;
        this.einstellungen = einstellungen;
    }

    public JPanel createPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton btnAnzeigen = new JButton("Statistik anzeigen");
        btnAnzeigen.addActionListener(getActionListenerBtnStatistikAnzeigen(panel));

        final JPanel datumPanel = new JPanel(new GridLayout(2, 2, 5, 0));
        datumPanel.setPreferredSize(new Dimension(350, 40));
        datumPanel.add(new JLabel("Rechnungsdatum von"));
        datumPanel.add(new JLabel("Rechnungsdatum bis"));
        datumPanel.add(zeitraumVon);
        datumPanel.add(zeitraumBis);
        panel.add(datumPanel);

        final Panel postenPanel = new Panel(new GridLayout(2, 1, 5, 0));
        postenPanel.add(new JLabel("Bezeichnung Rechnungsposten (optional)"));
        postenPanel.add(posten);
        panel.add(postenPanel);

        final Panel btnPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnAnzeigen);
        panel.add(btnPanel);

        presetFields();

        return panel;
    }

    private void presetFields() {
        final LocalDateTime firstDayOfYear = LocalDateTime.now().withDayOfYear(1);
        zeitraumVon
                .setText(KassenbuchErstellenUtils.DATE_FORMAT.format(Date.from(firstDayOfYear.atZone(ZoneId.systemDefault()).toInstant())));

        final LocalDateTime lastDayOfYear = LocalDateTime.now().withMonth(Month.DECEMBER.getValue()).withDayOfMonth(31);
        zeitraumBis
                .setText(KassenbuchErstellenUtils.DATE_FORMAT.format(Date.from(lastDayOfYear.atZone(ZoneId.systemDefault()).toInstant())));
        posten.setText("Service");
    }

    private ActionListener getActionListenerBtnStatistikAnzeigen(final JPanel panel) {
        return e -> {
            final Date dateFrom = parseDate(zeitraumVon.getText());
            final Date dateTo = parseDate(zeitraumBis.getText());
            if (dateFrom != null && dateTo != null) {
                final List<Rechnung> rechnungen = RechnungenEinlesenUtils
                        .readHtmlFilesWithPosten(new File(einstellungen.getRechnungsverzeichnisText()), dateFrom, dateTo);
                log.debug("{} Rechnungen ausgelesen", rechnungen.size());
                final File ablageverzeichnis = new File(einstellungen.getAblageverzeichnisText());
                try {
                    final Map<Integer, Map<Zahlart, Map<Month, List<Rechnung>>>> statistikProJahrZahlungsartMonat = KassenbuchStatistikUtils
                            .getStatistikProJahrZahlungsartMonat(rechnungen);
                    KassenbuchStatistikUtils.createFile(ablageverzeichnis, statistikProJahrZahlungsartMonat);

                    final Map<Integer, Map<String, Map<Month, BigDecimal>>> statistikProJahrPostenMonat = KassenbuchStatistikUtils
                            .getStatistikProJahrPostenMonat(rechnungen, posten.getText());
                    KassenbuchStatistikUtils.createPostenFile(ablageverzeichnis, statistikProJahrPostenMonat);
                    JOptionPane.showMessageDialog(main, "Statistiken wurden erzeugt und im Ablageverzeichnis abgelegt.");
                } catch (final IOException e1) {
                    log.error(e1.getMessage(), e1);
                    JOptionPane.showMessageDialog(main, "Fehler beim erzeugen der Statistik. Näheres ist der Log-Datei zu entnehmen.");
                }
            }
        };
    }

    public Date parseDate(final String date) {
        try {
            return KassenbuchErstellenUtils.DATE_FORMAT.parse(date);
        } catch (final ParseException e) {
            JOptionPane.showMessageDialog(main, "Das angegebene Datum besitzt kein gültiges Datumsformat. (dd.MM.yyyy)");
        }
        return null;
    }

}
