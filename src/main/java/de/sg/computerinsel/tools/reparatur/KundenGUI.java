package de.sg.computerinsel.tools.reparatur;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Kunde;

/**
 * @author Sita Geßner
 */
public class KundenGUI extends BaseEditGUI {

    private static final String[] COLUMNS = new String[] { "Nachname", "Vorname", "Straße", "PLZ", "Ort", "Telefon", "E-Mail" };

    private final JTextField nameFeld = new JTextField();

    private final JTextField vornameFeld = new JTextField();

    private final JTextField emailFeld = new JTextField();

    private final JTextField ortFeld = new JTextField();

    private final JTextField plzFeld = new JTextField();

    private final JTextField strasseFeld = new JTextField();

    private final JTextField telefonFeld = new JTextField();

    public KundenGUI(final HibernateService service) {
        super.service = service;

        main = new JFrame();
        main.setTitle("Reparaturprogramm - Kunden V1.0.0 © Sita Geßner");
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setSize(600, 800);
        main.setResizable(false);
        main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
        main.setVisible(true);
        main.add(createPanel());
    }

    private JPanel createPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(createTablePane(getTableMouseListener(), service.list(Kunde.class), COLUMNS));
        panel.add(createEditPanel());
        final JPanel btnPanel = createBtnPanel(getActionListenerBtnSpeichern(true), getActionListenerBtnSpeichern(false),
                getActionListenerBtnSuchen());
        addReparaturenBtn(btnPanel);
        panel.add(btnPanel);
        return panel;
    }

    private void addReparaturenBtn(final JPanel panel) {
        final JButton btnReparaturenAnzeigen = new JButton(new ImageIcon(getClass().getResource("pictures/zahnrad-2.png")));
        btnReparaturenAnzeigen.addActionListener(getActionListenerBtnReparaturenAnzeigen());
        btnReparaturenAnzeigen.setToolTipText("Reparaturen anzeigen");
        panel.add(btnReparaturenAnzeigen);
    }

    private ActionListener getActionListenerBtnReparaturenAnzeigen() {
        return e -> {
            new KundeReparaturGUI(service, (Kunde) getObj());
        };
    }

    private JPanel createEditPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 300));
        panel.setLayout(new GridLayout(14, 1));
        panel.add(new JLabel(COLUMNS[0]));
        panel.add(nameFeld);
        panel.add(new JLabel(COLUMNS[1]));
        panel.add(vornameFeld);
        panel.add(new JLabel(COLUMNS[2]));
        panel.add(strasseFeld);
        panel.add(new JLabel(COLUMNS[3]));
        panel.add(plzFeld);
        panel.add(new JLabel(COLUMNS[4]));
        panel.add(ortFeld);
        panel.add(new JLabel(COLUMNS[5]));
        panel.add(telefonFeld);
        panel.add(new JLabel(COLUMNS[6]));
        panel.add(emailFeld);
        return panel;
    }

    private ActionListener getActionListenerBtnSuchen() {
        return e -> {
            getTableModel().setRowCount(0);
            for (final IntegerBaseObject obj : service.listByConditions(Kunde.class,
                    service.createConditions(createKundeObjectFromFields(true)))) {
                getTableModel().addRow(obj.getTableModelObject());
            }
        };
    }

    private ActionListener getActionListenerBtnSpeichern(final boolean erstellen) {
        return e -> {
            if (StringUtils.isBlank(nameFeld.getText())) {
                JOptionPane.showMessageDialog(main, "Bitte geben Sie einen Namen an.");
            } else {
                final int reply = JOptionPane.showConfirmDialog(null, "Sollen der Kunde nun gespeichert werden?",
                        nameFeld.getText() + " speichern?", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    setObj(createKundeObjectFromFields(erstellen));
                    saveObj();
                }
            }
        };
    }

    private Kunde createKundeObjectFromFields(final boolean erstellen) {
        final Kunde kunde = getObj() instanceof Filiale && !erstellen ? (Kunde) getObj() : new Kunde();
        kunde.setNachname(StringUtils.stripToNull(nameFeld.getText()));
        kunde.setVorname(StringUtils.stripToNull(vornameFeld.getText()));
        kunde.setStrasse(StringUtils.stripToNull(strasseFeld.getText()));
        kunde.setPlz(StringUtils.stripToNull(plzFeld.getText()));
        kunde.setOrt(StringUtils.stripToNull(ortFeld.getText()));
        kunde.setTelefon(StringUtils.stripToNull(telefonFeld.getText()));
        kunde.setEmail(StringUtils.stripToNull(emailFeld.getText()));
        return kunde;
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
                if (row != null && row.size() == 8) {
                    final Kunde kunde = new Kunde();

                    final String name = (String) row.get(0);
                    kunde.setNachname(name);
                    nameFeld.setText(name);

                    final String vorname = (String) row.get(1);
                    kunde.setVorname(vorname);
                    vornameFeld.setText(vorname);

                    final String strasse = (String) row.get(2);
                    kunde.setStrasse(strasse);
                    strasseFeld.setText(strasse);

                    final String plz = (String) row.get(3);
                    kunde.setPlz(plz);
                    plzFeld.setText(plz);

                    final String ort = (String) row.get(4);
                    kunde.setOrt(ort);
                    ortFeld.setText(ort);

                    final String telefon = (String) row.get(5);
                    kunde.setTelefon(telefon);
                    telefonFeld.setText(telefon);

                    final String email = (String) row.get(5);
                    kunde.setEmail(email);
                    emailFeld.setText(email);

                    kunde.setId((Integer) row.get(7));
                    setObj(kunde);
                }

            }
        };
    }

}
