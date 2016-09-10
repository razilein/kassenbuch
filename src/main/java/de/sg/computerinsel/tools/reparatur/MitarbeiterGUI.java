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
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;

/**
 * @author Sita Geßner
 */
public class MitarbeiterGUI extends BaseEditGUI {

    private static final String[] COLUMNS = new String[] { "Nachname", "Vorname" };

    private final JTextField nameFeld = new JTextField();

    private final JTextField vornameFeld = new JTextField();

    MitarbeiterGUI(final HibernateService service) {
        super.service = service;
        super.main = main;
        main = new JFrame();
        main.setTitle("Reparaturprogramm - Mitarbeiter V1.0.0 © Sita Geßner");
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setSize(600, 500);
        main.setResizable(false);
        main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
        main.setVisible(true);
        main.add(createPanel());
    }

    private JPanel createPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(createTablePane(getTableMouseListener(), service.list(Mitarbeiter.class), COLUMNS));
        panel.add(createEditPanel());
        panel.add(createBtnPanel(getActionListenerBtnSpeichern(true), getActionListenerBtnSpeichern(false)));
        return panel;
    }

    private JPanel createEditPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 100));
        panel.setLayout(new GridLayout(4, 1));
        panel.add(new JLabel(COLUMNS[0]));
        panel.add(nameFeld);
        panel.add(new JLabel(COLUMNS[1]));
        panel.add(vornameFeld);
        return panel;
    }

    private ActionListener getActionListenerBtnSpeichern(final boolean erstellen) {
        return e -> {
            final Mitarbeiter mitarbeiter = getObj() instanceof Mitarbeiter && !erstellen ? (Mitarbeiter) getObj() : new Mitarbeiter();
            mitarbeiter.setNachname(StringUtils.stripToNull(nameFeld.getText()));
            mitarbeiter.setVorname(StringUtils.stripToNull(vornameFeld.getText()));
            setObj(mitarbeiter);
            saveObj();
        };
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
                if (row != null && row.size() == 3) {
                    final Mitarbeiter mitarbeiter = new Mitarbeiter();

                    final String nachname = (String) row.get(0);
                    mitarbeiter.setNachname(nachname);
                    nameFeld.setText(nachname);

                    final String vorname = (String) row.get(1);
                    mitarbeiter.setVorname(vorname);
                    vornameFeld.setText(vorname);

                    mitarbeiter.setId((Integer) row.get(2));
                    setObj(mitarbeiter);
                }

            }
        };
    }

}
