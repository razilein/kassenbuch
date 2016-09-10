package de.sg.computerinsel.tools.reparatur;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;

/**
 * @author Sita Geßners
 */
public class FilialeGUI {

    private final HibernateService service;

    private final JFrame main;

    private final DefaultTableModel tableModel = new DefaultTableModel() {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    private int selectedIndex;

    private Filiale selectedFiliale;

    private final JTextField emailFeld = new JTextField();

    private final JTextField nameFeld = new JTextField();

    private final JTextField ortFeld = new JTextField();

    private final JTextField plzFeld = new JTextField();

    private final JTextField strasseFeld = new JTextField();

    private final JTextField telefonFeld = new JTextField();

    FilialeGUI(final HibernateService service) {
        this.service = service;

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
        panel.add(createTablePane());
        panel.add(createEditPanel());
        panel.add(createBtnPanel());
        return panel;
    }

    private JPanel createBtnPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setPreferredSize(new Dimension(300, 100));

        final JButton btnLoeschen = new JButton("Löschen");
        btnLoeschen.addActionListener(getActionListenerBtnLoeschen());
        panel.add(btnLoeschen);

        final JButton btnSpeichern = new JButton("Speichern");
        btnSpeichern.addActionListener(getActionListenerBtnSpeichern());
        panel.add(btnSpeichern);

        return panel;
    }

    private ActionListener getActionListenerBtnLoeschen() {
        return e -> {
            final int reply = JOptionPane.showConfirmDialog(null, "Wollen Sie diese Filiale wirklich löschen?", "Filiale löschen?",
                    JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                service.delete(selectedFiliale);
                selectedFiliale.setId(null);
                tableModel.removeRow(selectedIndex);
                JOptionPane.showMessageDialog(main, "Die Filiale '" + selectedFiliale.getName() + "' wurde erfolgreich gelöscht.");
            }
        };
    }

    private ActionListener getActionListenerBtnSpeichern() {
        return e -> {
            final boolean isErstellen = selectedFiliale == null;
            if (isErstellen) {
                selectedFiliale = new Filiale();
            }
            selectedFiliale.setName(StringUtils.stripToNull(nameFeld.getText()));
            selectedFiliale.setEmail(StringUtils.stripToNull(emailFeld.getText()));
            selectedFiliale.setStrasse(StringUtils.stripToNull(strasseFeld.getText()));
            selectedFiliale.setPlz(StringUtils.stripToNull(plzFeld.getText()));
            selectedFiliale.setOrt(StringUtils.stripToNull(ortFeld.getText()));
            selectedFiliale.setTelefon(StringUtils.stripToNull(telefonFeld.getText()));
            final String errorMsg = validate(selectedFiliale);
            if (StringUtils.isBlank(errorMsg)) {
                selectedFiliale = (Filiale) service.save(selectedFiliale);
                JOptionPane.showMessageDialog(main, "Die Filiale '" + selectedFiliale.getName() + "' wurde erfolgreich gespeichert.");
            } else {
                JOptionPane.showMessageDialog(main, errorMsg);
            }
        };
    }

    private String validate(final Filiale filiale) {
        final StringBuilder errorMsg = new StringBuilder();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<Filiale>> validate = validator.validate(filiale);
        if (!validate.isEmpty()) {
            errorMsg.append("<html>");
        }
        for (final ConstraintViolation<Filiale> violation : validate) {
            errorMsg.append(violation.getMessage());
            errorMsg.append("<br>");
        }
        if (!validate.isEmpty()) {
            errorMsg.append("</html>");
        }
        return errorMsg.toString();
    }

    private JPanel createEditPanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 200));
        panel.setLayout(new GridLayout(12, 1));
        panel.add(new JLabel("Name"));
        panel.add(nameFeld);
        panel.add(new JLabel("E-Mail"));
        panel.add(emailFeld);
        panel.add(new JLabel("Straße"));
        panel.add(strasseFeld);
        panel.add(new JLabel("PLZ"));
        panel.add(plzFeld);
        panel.add(new JLabel("Ort"));
        panel.add(ortFeld);
        panel.add(new JLabel("Telefon"));
        panel.add(telefonFeld);
        return panel;
    }

    private JScrollPane createTablePane() {
        final JScrollPane pane = new JScrollPane();

        table.addMouseListener(getTableMouseListener());
        prepareTableModel();
        final List<IntegerBaseObject> list = service.list(Filiale.class);
        for (final IntegerBaseObject obj : list) {
            if (obj instanceof Filiale) {
                final Filiale filiale = (Filiale) obj;
                tableModel.addRow(new Object[] { filiale.getName(), filiale.getEmail(), filiale.getStrasse(), filiale.getPlz(),
                        filiale.getOrt(), filiale.getTelefon(), filiale.getId() });
            }
        }
        pane.setPreferredSize(new Dimension(600, list.size() * 30));
        pane.getViewport().add(table);
        return pane;
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
                selectedIndex = table.rowAtPoint(e.getPoint());
                final Vector<?> row = (Vector<?>) tableModel.getDataVector().elementAt(selectedIndex);
                if (row != null && row.size() == 7) {
                    selectedFiliale = new Filiale();

                    final String name = (String) row.get(0);
                    selectedFiliale.setName(name);
                    nameFeld.setText(name);

                    final String email = (String) row.get(1);
                    selectedFiliale.setEmail(email);
                    emailFeld.setText(email);

                    final String strasse = (String) row.get(2);
                    selectedFiliale.setStrasse(strasse);
                    strasseFeld.setText(strasse);

                    final String plz = (String) row.get(3);
                    selectedFiliale.setPlz(plz);
                    plzFeld.setText(plz);

                    final String ort = (String) row.get(4);
                    selectedFiliale.setOrt(ort);
                    ortFeld.setText(ort);

                    final String telefon = (String) row.get(5);
                    selectedFiliale.setTelefon(telefon);
                    telefonFeld.setText(telefon);

                    selectedFiliale.setId((Integer) row.get(6));
                }

            }
        };
    }

    private void prepareTableModel() {
        tableModel.addColumn("Name");
        tableModel.addColumn("E-Mail");
        tableModel.addColumn("Straße");
        tableModel.addColumn("PLZ");
        tableModel.addColumn("Ort");
        tableModel.addColumn("Telefon");
        tableModel.addColumn("ID");
        final TableColumnModel columnModel = table.getColumnModel();
        columnModel.removeColumn(columnModel.getColumn(6));
    }

}
