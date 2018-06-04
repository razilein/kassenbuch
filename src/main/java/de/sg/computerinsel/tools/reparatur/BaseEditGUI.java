package de.sg.computerinsel.tools.reparatur;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;

/**
 * @author Sita Geßner
 */
public class BaseEditGUI {

    public class DropDownItem {

        private Integer id;

        private String text;

        public DropDownItem() {
            this(null, null);
        }

        public DropDownItem(final Integer id, final String text) {
            this.id = id;
            this.text = text;
        }

        public Integer getId() {
            return id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

    }

    protected HibernateService service;

    protected JFrame main;

    private final DefaultTableModel tableModel = new DefaultTableModel() {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    private int selectedIndex;

    private IntegerBaseObject obj;

    private void prepareTableModel(final String... colNames) {
        for (final String name : colNames) {
            tableModel.addColumn(name);
        }
        tableModel.addColumn("ID");
        final TableColumnModel columnModel = table.getColumnModel();
        columnModel.removeColumn(columnModel.getColumn(colNames.length));
    }

    protected JScrollPane createTablePane(final MouseListener listener, final List<? extends IntegerBaseObject> list,
            final String[] columns) {
        return createTablePane(listener, list, columns, false);
    }

    protected JScrollPane createTablePane(final MouseListener listener, final List<? extends IntegerBaseObject> list,
            final String[] columns, final boolean searchTableObj) {
        final JScrollPane pane = new JScrollPane();

        table.addMouseListener(listener);
        prepareTableModel(columns);
        for (final IntegerBaseObject obj : list) {
            tableModel.addRow(searchTableObj ? obj.getTableModelObjectSearch() : obj.getTableModelObject());
        }
        final int height = list.size() > 10 || list.isEmpty() ? 200 : list.size() * 35;
        pane.setPreferredSize(new Dimension(main.getWidth(), height));
        pane.getViewport().add(table);
        return pane;
    }

    protected String validate(final IntegerBaseObject obj) {
        final StringBuilder errorMsg = new StringBuilder();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<IntegerBaseObject>> validate = validator.validate(obj);
        if (!validate.isEmpty()) {
            errorMsg.append("<html>");
        }
        for (final ConstraintViolation<IntegerBaseObject> violation : validate) {
            errorMsg.append(violation.getMessage());
            errorMsg.append("<br>");
        }
        if (!validate.isEmpty()) {
            errorMsg.append("</html>");
        }
        return errorMsg.toString();
    }

    protected JPanel createBtnPanel(final ActionListener listenerBtnErstellen, final ActionListener listenerBtnSpeichern,
            final JButton... buttons) {
        return createBtnPanel(listenerBtnErstellen, listenerBtnSpeichern, null, buttons);
    }

    protected JPanel createBtnPanel(final ActionListener listenerBtnErstellen, final ActionListener listenerBtnSpeichern,
            final ActionListener listenerBtnSuchen, final JButton... buttons) {
        final int columns = listenerBtnSuchen == null ? 3 : 4;
        final JPanel panel = new JPanel(new GridLayout(1, buttons == null ? columns : columns + buttons.length, 5, 5));
        panel.setPreferredSize(new Dimension(300, 50));

        if (listenerBtnSuchen != null) {
            final JButton btnSuchen = new JButton(new ImageIcon(getClass().getResource("pictures/suchen.png")));
            btnSuchen.addActionListener(listenerBtnSuchen);
            btnSuchen.setToolTipText(
                    "<html>Suchen<br>* = Platzhalter beliebig viele oder keine Zeichen<br>_ = Platzhalter ein Zeichen</html>");
            panel.add(btnSuchen);
        }

        final JButton btnErstellen = new JButton(new ImageIcon(getClass().getResource("pictures/erstellen.png")));
        btnErstellen.addActionListener(listenerBtnErstellen);
        btnErstellen.setToolTipText("Erstellen");
        panel.add(btnErstellen);

        final JButton btnLoeschen = new JButton(new ImageIcon(getClass().getResource("pictures/loeschen.png")));
        btnLoeschen.addActionListener(getActionListenerBtnLoeschen());
        btnLoeschen.setToolTipText("Löschen");
        panel.add(btnLoeschen);

        final JButton btnSpeichern = new JButton(new ImageIcon(getClass().getResource("pictures/speichern.png")));
        btnSpeichern.addActionListener(listenerBtnSpeichern);
        btnSpeichern.setToolTipText("Speichern");
        panel.add(btnSpeichern);
        if (buttons != null) {
            for (final JButton btn : buttons) {
                panel.add(btn);
            }
        }
        return panel;
    }

    private ActionListener getActionListenerBtnLoeschen() {
        return e -> {
            final int reply = JOptionPane.showConfirmDialog(null, "Wollen Sie diesen Eintrag wirklich löschen?",
                    obj.toString() + " löschen?", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                service.delete(obj);
                obj.setId(null);
                tableModel.removeRow(selectedIndex);
                JOptionPane.showMessageDialog(main, obj.toString() + " wurde erfolgreich gelöscht.");
            }
        };
    }

    protected void saveObj() {
        final boolean isErstellen = obj.getId() == null;
        if (isErstellen && obj instanceof Reparatur) {
            final Reparatur reparatur = (Reparatur) obj;
            reparatur.setNummer(createAuftragsnummer());
        }
        final String errorMsg = validate(obj);
        if (StringUtils.isBlank(errorMsg)) {
            obj = service.save(obj);
            JOptionPane.showMessageDialog(main, obj.toString() + " wurde erfolgreich gespeichert.");
            if (isErstellen) {
                tableModel.addRow(obj.getTableModelObject());
            } else {
                tableModel.fireTableDataChanged();
            }
        } else {
            JOptionPane.showMessageDialog(main, errorMsg);
        }
    }

    private String createAuftragsnummer() {
        final Integer nummer = service.getMaxId(Reparatur.class);
        final Filiale filiale = (Filiale) service.get(Filiale.class, SettingsUtils.getFiliale());
        final String kuerzel = filiale == null || filiale.getKuerzel() == null ? StringUtils.EMPTY : filiale.getKuerzel();
        return kuerzel + String.valueOf(nummer);
    }

    protected Vector<?> getRow(final Point point) {
        selectedIndex = table.rowAtPoint(point);
        return (Vector<?>) tableModel.getDataVector().elementAt(selectedIndex);
    }

    public IntegerBaseObject getObj() {
        return obj;
    }

    public void setObj(final IntegerBaseObject obj) {
        this.obj = obj;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(final int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

}
