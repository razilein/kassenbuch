package de.sg.computerinsel.tools.reparatur;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.ReparaturArt;

/**
 * @author Sita Geßner
 */
public class KundeReparaturGUI extends BaseEditGUI {

    private static final String[] COLUMNS = new String[] { "Nummer", "Art", "Gerät", "Seriennummer", "Symptome / Fehler",
            "Geplante Aufgaben", "Gerätepasswort", "Expressbearbeitung", "Abholdatum", "Abholzeit", "Kostenvoranschlag", "Mitarbeiter" };

    private final JTextField nummerFeld = new JTextField();

    private final JComboBox<ReparaturArt> artFeld = new JComboBox<>(ReparaturArt.values());

    private final JTextField geraetFeld = new JTextField();

    private final JTextField seriennummerFeld = new JTextField();

    private final JTextField symptomeFeld = new JTextField();

    private final JTextField aufgabenFeld = new JTextField();

    private final JTextField geraetepasswortFeld = new JTextField();

    private final JCheckBox expressbeabeitungFeld = new JCheckBox();

    private final JTextField abholdatumFeld = new JTextField();

    private final JTextField abholzeitFeld = new JTextField();

    private final JTextField kostenvoranschlagFeld = new JTextField();

    private final JTextField mitarbeiterFeld = new JTextField();

    private final Kunde kunde;

    public KundeReparaturGUI(final HibernateService service, final Kunde kunde) {
        super.service = service;
        this.kunde = kunde;

        main = new JFrame();
        main.setTitle("Reparaturprogramm - Kunden V1.0.0 © Sita Geßner");
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setSize(900, 800);
        main.setResizable(false);
        main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
        main.setVisible(true);
        main.add(createPanel());
    }

    private JPanel createPanel() {
        final JPanel panel = new JPanel();
        panel.add(createTablePane(getTableMouseListener(),
                service.listByConditions(Reparatur.class, Collections.singletonMap("kunde.id", kunde.getId())), COLUMNS));
        panel.add(createEditPanel());
        panel.add(createBtnPanel(getActionListenerBtnSpeichern(true), getActionListenerBtnSpeichern(false)));
        return panel;
    }

    private ActionListener getActionListenerBtnSpeichern(final boolean erstellen) {
        return e -> {
            final Reparatur reparatur = getObj() instanceof Reparatur && !erstellen ? (Reparatur) getObj() : new Reparatur();
            reparatur.setNummer(nummerFeld.getText());
            reparatur.setArt(artFeld.getSelectedIndex());
            reparatur.setGeraet(StringUtils.stripToNull(geraetFeld.getText()));
            reparatur.setSeriennummer(StringUtils.stripToNull(seriennummerFeld.getText()));
            reparatur.setSymptome(StringUtils.stripToNull(symptomeFeld.getText()));
            reparatur.setAufgaben(StringUtils.stripToNull(aufgabenFeld.getText()));
            reparatur.setGeraetepasswort(StringUtils.stripToNull(geraetepasswortFeld.getText()));
            reparatur.setExpressbeabeitung(expressbeabeitungFeld.isSelected());
            // filiale.setAbholdatum(StringUtils.stripToNull(abholdatumFeld.getText()));
            // filiale.setAbholzeit(StringUtils.stripToNull(abholzeitFeld.getText()));
            reparatur.setKostenvoranschlag(StringUtils.stripToNull(kostenvoranschlagFeld.getText()));
            setObj(reparatur);
            saveObj();
        };
    }

    private JPanel createEditPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 400));
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
        panel.add(expressbeabeitungFeld);
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
                    artFeld.setSelectedIndex(art);

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
                    reparatur.setExpressbeabeitung(expressbeabeitung);
                    expressbeabeitungFeld.setSelected(expressbeabeitung);

                    final Date abholdatum = (Date) row.get(8);
                    // TODO Abholdatum
                    reparatur.setAbholdatum(abholdatum);
                    abholdatumFeld.setText(abholdatum.toString());

                    final Time abholzeit = (Time) row.get(9);
                    // TODO Abholzeit
                    reparatur.setAbholzeit(abholzeit);
                    abholzeitFeld.setText(abholzeit.toString());

                    final String kostenvoranschlag = (String) row.get(10);
                    reparatur.setKostenvoranschlag(kostenvoranschlag);
                    kostenvoranschlagFeld.setText(kostenvoranschlag);

                    final Mitarbeiter mitarbeiter = (Mitarbeiter) row.get(11);
                    reparatur.setMitarbeiter(mitarbeiter);
                    mitarbeiterFeld.setText(mitarbeiter.toString());

                    reparatur.setId((Integer) row.get(12));
                    setObj(reparatur);
                }

            }
        };
    }

}
