package de.sg.computerinsel.tools.reparatur;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.ReportService;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.ReparaturArt;

/**
 * @author Sita Geßner
 */
public class KundeReparaturGUI extends BaseEditGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(KundeReparaturGUI.class);

    private static final String[] COLUMNS = new String[] { "Nummer", "Art", "Gerät", "Seriennummer", "Symptome / Fehler",
            "Geplante Aufgaben", "Gerätepasswort", "Expressbearbeitung", "Abholdatum", "Abholzeit", "Kostenvoranschlag", "Mitarbeiter" };

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final SimpleDateFormat DATE = new SimpleDateFormat("dd.MM.yyyy");

    private static final SimpleDateFormat TIME = new SimpleDateFormat("HH:mm");

    private final JTextField nummerFeld = new JTextField();

    private final JComboBox<DropDownItem> artFeld = new JComboBox<>();

    private final JTextField geraetFeld = new JTextField();

    private final JTextField seriennummerFeld = new JTextField();

    private final JTextField symptomeFeld = new JTextField();

    private final JTextField aufgabenFeld = new JTextField();

    private final JTextField geraetepasswortFeld = new JTextField();

    private final JCheckBox expressbearbeitungFeld = new JCheckBox();

    private final JTextField abholdatumFeld = new JTextField();

    private final JTextField abholzeitFeld = new JTextField();

    private final JTextField kostenvoranschlagFeld = new JTextField();

    private final JComboBox<DropDownItem> mitarbeiterFeld = new JComboBox<>();

    private final Kunde kunde;

    private final ReportService reportService;

    public KundeReparaturGUI(final HibernateService service, final Kunde kunde) {
        super.service = service;
        this.kunde = kunde;
        reportService = new ReportService(service.getConnectionProperties());
        service.list(Mitarbeiter.class)
                .forEach(m -> mitarbeiterFeld.addItem(new DropDownItem(((Mitarbeiter) m).getId(), ((Mitarbeiter) m).getCompleteName())));
        Arrays.asList(ReparaturArt.values()).forEach(r -> artFeld.addItem(new DropDownItem(r.getCode(), r.getDescription())));
        main = new JFrame();
        main.setTitle("Reparaturprogramm - Kunden V1.0.0 © Sita Geßner");
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setSize(900, 800);
        main.setResizable(false);
        main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
        main.setVisible(true);
        main.add(createPanel());
    }

    public KundeReparaturGUI(final HibernateService service, final JFrame main) {
        super.service = service;
        this.kunde = null;
        reportService = new ReportService(service.getConnectionProperties());
        service.list(Mitarbeiter.class)
                .forEach(m -> mitarbeiterFeld.addItem(new DropDownItem(((Mitarbeiter) m).getId(), ((Mitarbeiter) m).getCompleteName())));
        Arrays.asList(ReparaturArt.values()).forEach(r -> artFeld.addItem(new DropDownItem(r.getCode(), r.getDescription())));
        this.main = main;
        this.main.add(createPanel());
    }

    private JPanel createPanel() {
        final JPanel panel = new JPanel();
        if (kunde != null) {
            panel.add(createTablePane(getTableMouseListener(),
                    service.listByConditions(Reparatur.class, Collections.singletonMap("kunde.id", kunde.getId())), COLUMNS));
        }
        panel.add(createEditPanel());
        initFelder();

        final JButton btnDownload = new JButton(new ImageIcon(getClass().getResource("pictures/download.png")));
        btnDownload.addActionListener(getActionListenerShowReport());
        btnDownload.setToolTipText("Download PDF");

        panel.add(createBtnPanel(getActionListenerBtnSpeichern(true), getActionListenerBtnSpeichern(false), btnDownload));
        return panel;
    }

    private void initFelder() {
        final LocalDate abholdatum = LocalDate.now().plusWeeks(1);
        if (abholdatum.getDayOfWeek() == DayOfWeek.SUNDAY) {
            abholdatum.plusDays(1);
        }
        abholdatumFeld.setText(abholdatum.format(DATETIME_FORMATTER));
        abholzeitFeld.setText("12:00");

        nummerFeld.setPreferredSize(new Dimension(20, 10));
    }

    private ActionListener getActionListenerShowReport() {
        return e -> {
            if (Desktop.isDesktopSupported()) {
                for (final File reportFile : reportService.createReport(getObj().getId().intValue(), SettingsUtils.getFiliale())) {
                    if (reportFile.exists()) {
                        try {
                            Desktop.getDesktop().open(reportFile);
                        } catch (final IOException e1) {
                            LOGGER.error(e1.getMessage(), e);
                        }
                    }
                }
            }
        };
    }

    private ActionListener getActionListenerBtnSpeichern(final boolean erstellen) {
        return e -> {
            final Reparatur reparatur = getObj() instanceof Reparatur && !erstellen ? (Reparatur) getObj() : new Reparatur();
            reparatur.setNummer(nummerFeld.getText());
            DropDownItem item = (DropDownItem) artFeld.getSelectedItem();
            reparatur.setArt(ReparaturArt.getByCode(item.getId()).getCode());
            reparatur.setGeraet(StringUtils.stripToNull(geraetFeld.getText()));
            reparatur.setSeriennummer(StringUtils.stripToNull(seriennummerFeld.getText()));
            reparatur.setSymptome(StringUtils.stripToNull(symptomeFeld.getText()));
            reparatur.setAufgaben(StringUtils.stripToNull(aufgabenFeld.getText()));
            reparatur.setGeraetepasswort(StringUtils.stripToNull(geraetepasswortFeld.getText()));
            reparatur.setExpressbearbeitung(expressbearbeitungFeld.isSelected());
            reparatur.setKostenvoranschlag(StringUtils.stripToNull(kostenvoranschlagFeld.getText()));
            final Mitarbeiter mitarbeiter = new Mitarbeiter();
            item = (DropDownItem) mitarbeiterFeld.getSelectedItem();
            mitarbeiter.setId(item == null ? null : item.getId());
            reparatur.setMitarbeiter(mitarbeiter);
            reparatur.setKunde(kunde);
            try {
                reparatur.setAbholdatum(DATE.parse(StringUtils.stripToNull(abholdatumFeld.getText())));
                final String time = StringUtils.stripToNull(abholzeitFeld.getText());
                if (time != null) {
                    reparatur.setAbholzeit(new Time(TIME.parse(time).getTime()));
                }
                final int reply = JOptionPane.showConfirmDialog(null, "Soll der Auftrag nun gespeichert werden?",
                        nummerFeld.getText() + " speichern?", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    setObj(reparatur);
                    saveObj();
                }
            } catch (final ParseException e1) {
                LOGGER.debug(e1.getMessage(), e1);
                JOptionPane.showMessageDialog(main,
                        "Bitte geben Sie eine gültiges Abholdatum (Format dd.MM.yyyy) und eine gültige Abholzeit (Format HH:mm) an.");
            }
        };
    }

    private JPanel createEditPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 400));
        panel.setLayout(new GridLayout(20, 1));
        panel.add(new JLabel(COLUMNS[0]));
        panel.add(nummerFeld);
        nummerFeld.setEditable(false);
        panel.add(new JLabel(COLUMNS[1]));
        panel.add(artFeld);
        panel.add(new JLabel(COLUMNS[2]));
        panel.add(geraetFeld);
        panel.add(new JLabel(COLUMNS[3]));
        panel.add(seriennummerFeld);
        panel.add(new JLabel(COLUMNS[4]));
        panel.add(symptomeFeld);
        panel.add(new JLabel(COLUMNS[5]));
        panel.add(aufgabenFeld);
        panel.add(new JLabel(COLUMNS[6]));
        panel.add(geraetepasswortFeld);
        panel.add(new JLabel(COLUMNS[7]));
        panel.add(expressbearbeitungFeld);
        panel.add(new JLabel(COLUMNS[8]));
        panel.add(abholdatumFeld);
        panel.add(new JLabel(COLUMNS[9]));
        panel.add(abholzeitFeld);
        panel.add(new JLabel(COLUMNS[10]));
        panel.add(kostenvoranschlagFeld);
        panel.add(new JLabel(COLUMNS[11]));
        panel.add(mitarbeiterFeld);
        return panel;
    }

    private MouseListener getTableMouseListener() {
        return new MouseListener() {

            @Override
            public void mouseReleased(final MouseEvent e) {
                // do nothing
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                final Vector<?> row = getRow(e.getPoint());
                if (row != null && row.size() == 13) {
                    final Reparatur reparatur = new Reparatur();

                    final String nummer = (String) row.get(0);
                    reparatur.setNummer(nummer);
                    nummerFeld.setText(nummer);

                    final Integer art = (Integer) row.get(1);
                    reparatur.setArt(art);
                    setArtFeld(art);

                    final String geraet = (String) row.get(2);
                    reparatur.setGeraet(geraet);
                    geraetFeld.setText(geraet);

                    final String seriennummer = (String) row.get(3);
                    reparatur.setSeriennummer(seriennummer);
                    seriennummerFeld.setText(seriennummer);

                    final String symptome = (String) row.get(4);
                    reparatur.setSymptome(symptome);
                    symptomeFeld.setText(symptome);

                    final String aufgaben = (String) row.get(5);
                    reparatur.setAufgaben(aufgaben);
                    aufgabenFeld.setText(aufgaben);

                    final String geraetepasswort = (String) row.get(6);
                    reparatur.setGeraetepasswort(geraetepasswort);
                    geraetepasswortFeld.setText(geraetepasswort);

                    final Boolean expressbeabeitung = (Boolean) row.get(7);
                    reparatur.setExpressbearbeitung(expressbeabeitung);
                    expressbearbeitungFeld.setSelected(expressbeabeitung);

                    final Date abholdatum = (Date) row.get(8);
                    reparatur.setAbholdatum(abholdatum);
                    abholdatumFeld.setText(DATE.format(abholdatum));

                    final Time abholzeit = (Time) row.get(9);
                    reparatur.setAbholzeit(abholzeit);
                    abholzeitFeld.setText(TIME.format(abholzeit));

                    final String kostenvoranschlag = (String) row.get(10);
                    reparatur.setKostenvoranschlag(kostenvoranschlag);
                    kostenvoranschlagFeld.setText(kostenvoranschlag);

                    final Mitarbeiter mitarbeiter = (Mitarbeiter) row.get(11);
                    reparatur.setMitarbeiter(mitarbeiter);
                    setMitarbeiterFeld(mitarbeiter);

                    reparatur.setId((Integer) row.get(12));
                    reparatur.setKunde(kunde);
                    setObj(reparatur);
                }

            }

            private void setMitarbeiterFeld(final Mitarbeiter mitarbeiter) {
                for (int i = 0; i < mitarbeiterFeld.getItemCount(); i++) {
                    final DropDownItem item = mitarbeiterFeld.getItemAt(i);
                    if (mitarbeiter.getId().equals(item.getId())) {
                        mitarbeiterFeld.setSelectedIndex(i);
                    }
                }
            }

            private void setArtFeld(final Integer art) {
                for (int i = 0; i < artFeld.getItemCount(); i++) {
                    final DropDownItem item = artFeld.getItemAt(i);
                    if (art.equals(item.getId())) {
                        artFeld.setSelectedIndex(i);
                    }
                }
            }
        };
    }
}
