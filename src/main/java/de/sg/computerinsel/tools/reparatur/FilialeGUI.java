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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.reparatur.model.Filiale;

/**
 * @author Sita Geßners
 */
public class FilialeGUI extends BaseEditGUI {

    private static final String[] COLUMNS = new String[] { "Name", "Kürzel (Auftragsnummer)", "E-Mail", "Straße", "PLZ", "Ort", "Telefon" };

    private final JTextField emailFeld = new JTextField();

    private final JTextField nameFeld = new JTextField();

    private final JTextField ortFeld = new JTextField();

    private final JTextField plzFeld = new JTextField();

    private final JTextField strasseFeld = new JTextField();

    private final JTextField telefonFeld = new JTextField();

    private final JTextField kuerzelFeld = new JTextField();

    FilialeGUI(final HibernateService service) {
        super.service = service;

        main = new JFrame();
        main.setTitle("Reparaturprogramm - Filialien V1.0.0 © Sita Geßner");
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setSize(600, 500);
        main.setResizable(false);
        main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
        main.setVisible(true);
        main.add(createPanel());
    }

    private JPanel createPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(createTablePane(getTableMouseListener(), service.list(Filiale.class), COLUMNS));
        panel.add(createEditPanel());
        panel.add(createBtnPanel(getActionListenerBtnSpeichern(true), getActionListenerBtnSpeichern(false)));
        return panel;
    }

    private ActionListener getActionListenerBtnSpeichern(final boolean erstellen) {
        return e -> {
            final Filiale filiale = getObj() instanceof Filiale && !erstellen ? (Filiale) getObj() : new Filiale();
            filiale.setName(StringUtils.stripToNull(nameFeld.getText()));
            filiale.setKuerzel(StringUtils.stripToNull(kuerzelFeld.getText()));
            filiale.setEmail(StringUtils.stripToNull(emailFeld.getText()));
            filiale.setStrasse(StringUtils.stripToNull(strasseFeld.getText()));
            filiale.setPlz(StringUtils.stripToNull(plzFeld.getText()));
            filiale.setOrt(StringUtils.stripToNull(ortFeld.getText()));
            filiale.setTelefon(StringUtils.stripToNull(telefonFeld.getText()));

            if (StringUtils.isAnyBlank(filiale.getName(), filiale.getKuerzel(), filiale.getEmail(), filiale.getStrasse(), filiale.getPlz(),
                    filiale.getOrt(), filiale.getTelefon())) {
                JOptionPane.showMessageDialog(main, "Bitte füllen Sie alle Felder aus!");
            } else {
                final int reply = JOptionPane.showConfirmDialog(null, "Soll die Filiale nun gespeichert werden?",
                        nameFeld.getText() + " speichern?", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    setObj(filiale);
                    saveObj();
                }
            }
        };
    }

    private JPanel createEditPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 240));
        panel.setLayout(new GridLayout(14, 1));
        panel.add(new JLabel(COLUMNS[0]));
        panel.add(nameFeld);
        panel.add(new JLabel(COLUMNS[1]));
        panel.add(kuerzelFeld);
        panel.add(new JLabel(COLUMNS[2]));
        panel.add(emailFeld);
        panel.add(new JLabel(COLUMNS[3]));
        panel.add(strasseFeld);
        panel.add(new JLabel(COLUMNS[4]));
        panel.add(plzFeld);
        panel.add(new JLabel(COLUMNS[5]));
        panel.add(ortFeld);
        panel.add(new JLabel(COLUMNS[6]));
        panel.add(telefonFeld);
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
                if (row != null && row.size() == 8) {
                    final Filiale filiale = new Filiale();

                    final String name = (String) row.get(0);
                    filiale.setName(name);
                    nameFeld.setText(name);

                    final String kuerzel = (String) row.get(1);
                    filiale.setKuerzel(kuerzel);
                    kuerzelFeld.setText(kuerzel);

                    final String email = (String) row.get(2);
                    filiale.setEmail(email);
                    emailFeld.setText(email);

                    final String strasse = (String) row.get(3);
                    filiale.setStrasse(strasse);
                    strasseFeld.setText(strasse);

                    final String plz = (String) row.get(4);
                    filiale.setPlz(plz);
                    plzFeld.setText(plz);

                    final String ort = (String) row.get(5);
                    filiale.setOrt(ort);
                    ortFeld.setText(ort);

                    final String telefon = (String) row.get(6);
                    filiale.setTelefon(telefon);
                    telefonFeld.setText(telefon);

                    filiale.setId((Integer) row.get(7));
                    setObj(filiale);
                }

            }
        };
    }

}
