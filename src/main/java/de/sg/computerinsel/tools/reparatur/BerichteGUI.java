package de.sg.computerinsel.tools.reparatur;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.ReportService;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;

/**
 * @author Sita Geßners
 */
public class BerichteGUI extends BaseEditGUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(BerichteGUI.class);

    private static final String[] COLUMNS = new String[] { "Nummer", "Art", "Gerät", "Abholdatum", "Abholzeit", "Kunde", "Mitarbeiter" };

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private final JTextField datumVonFeld = new JTextField(LocalDate.now().minusDays(1).format(DATE));

    private final JTextField datumBisFeld = new JTextField(LocalDate.now().plusDays(5).format(DATE));

    private final JCheckBox erledigteAnzeigenFeld = new JCheckBox();

    private final ReportService reportService;

    BerichteGUI(final HibernateService service) {
        super.service = service;
        reportService = new ReportService(service.getConnectionProperties());

        main = new JFrame();
        main.setTitle("Reparaturprogramm - Was geht? V1.0.0 © Sita Geßner");
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setSize(600, 500);
        main.setResizable(false);
        main.dispatchEvent(new WindowEvent(main, WindowEvent.WINDOW_CLOSING));
        main.setVisible(true);
        main.add(createPanel());
    }

    private JPanel createPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(createTablePane(getTableMouseListener(), listReparaturByConditions(), COLUMNS, true));
        panel.add(createSuchePanel());
        panel.add(createBtnPanel(getActionListenerBtnSuchen(), getActionListenerBtnAnzeigen(), getActionListenerBtnErledigen()));
        return panel;
    }

    @Override
    protected JPanel createBtnPanel(final ActionListener listenerBtnSuchen, final ActionListener listenerBtnAnzeigen,
            final ActionListener listenerBtnErledigen, final JButton... buttons) {
        final int columns = 3;
        final JPanel panel = new JPanel(new GridLayout(1, columns, 5, 5));
        panel.setPreferredSize(new Dimension(300, 50));

        final JButton btnSuchen = new JButton(new ImageIcon(getClass().getResource("pictures/suchen.png")));
        btnSuchen.addActionListener(listenerBtnSuchen);
        btnSuchen.setToolTipText("<html>Suchen<br>* = Platzhalter beliebig viele oder keine Zeichen<br>_ = Platzhalter ein Zeichen</html>");
        panel.add(btnSuchen);

        final JButton btnAnzeigen = new JButton(new ImageIcon(getClass().getResource("pictures/download.png")));
        btnAnzeigen.addActionListener(listenerBtnAnzeigen);
        btnAnzeigen.setToolTipText("Anzeigen");
        panel.add(btnAnzeigen);

        final JButton btnErledigen = new JButton(new ImageIcon(getClass().getResource("pictures/erledigen.png")));
        btnErledigen.addActionListener(listenerBtnErledigen);
        btnErledigen.setToolTipText("Markierte Aufträge erledigen");
        panel.add(btnErledigen);
        if (buttons != null) {
            for (final JButton btn : buttons) {
                panel.add(btn);
            }
        }
        return panel;
    }

    private ActionListener getActionListenerBtnSuchen() {
        return e -> {
            getTableModel().setRowCount(0);
            for (final IntegerBaseObject obj : listReparaturByConditions()) {
                getTableModel().addRow(obj.getTableModelObjectSearch());
            }
        };
    }

    private List<IntegerBaseObject> listReparaturByConditions() {
        List<IntegerBaseObject> result = new ArrayList<>();
        try {
            final Date datumVon = StringUtils.isBlank(datumVonFeld.getText()) ? null
                    : DATE_FORMATTER.parse(StringUtils.stripToNull(datumVonFeld.getText()));
            final Date datumBis = StringUtils.isBlank(datumBisFeld.getText()) ? null
                    : DATE_FORMATTER.parse(StringUtils.stripToNull(datumBisFeld.getText()));
            final Reparatur reparatur = new Reparatur();
            reparatur.setErledigt(erledigteAnzeigenFeld.isSelected() ? null : false);
            result = service.listByDate(datumVon, datumBis, service.createConditions(reparatur));
        } catch (final ParseException e1) {
            LOGGER.debug(e1.getMessage(), e1);
            JOptionPane.showMessageDialog(main,
                    "Bitte geben Sie eine gültiges Abholdatum (Format dd.MM.yyyy) und eine gültige Abholzeit (Format HH:mm) an.");
        }
        return result;
    }

    private ActionListener getActionListenerBtnAnzeigen() {
        return e -> {
            try {
                final Date datumVon = DATE_FORMATTER.parse(
                        StringUtils.isBlank(datumVonFeld.getText()) ? "19.11.1990" : StringUtils.stripToNull(datumVonFeld.getText()));
                final Date datumBis = DATE_FORMATTER.parse(
                        StringUtils.isBlank(datumBisFeld.getText()) ? "01.01.3000" : StringUtils.stripToNull(datumBisFeld.getText()));
                final File reportFile = reportService.createReportAuftragsuebersicht(datumVon, datumBis, SettingsUtils.getFiliale());
                if (reportFile != null && reportFile.exists()) {
                    try {
                        Desktop.getDesktop().open(reportFile);
                    } catch (final IOException e1) {
                        LOGGER.error(e1.getMessage(), e);
                    }
                }
            } catch (final ParseException e1) {
                LOGGER.debug(e1.getMessage(), e1);
                JOptionPane.showMessageDialog(main,
                        "Bitte geben Sie eine gültiges Abholdatum (Format dd.MM.yyyy) und eine gültige Abholzeit (Format HH:mm) an.");
            }
        };
    }

    private ActionListener getActionListenerBtnErledigen() {
        return e -> {
            int erledigt = 0;
            int bereitsErledigt = 0;
            final int[] selectedRows = getTable().getSelectedRows();
            for (final int rowIndex : selectedRows) {
                final Vector<?> row = (Vector<?>) getTableModel().getDataVector().elementAt(rowIndex);
                final Integer id = (Integer) row.get(7);
                final Reparatur reparatur = (Reparatur) service.get(Reparatur.class, id);
                if (reparatur.getErledigt()) {
                    bereitsErledigt++;
                } else {
                    reparatur.setErledigt(true);
                    reparatur.setErledigungsdatum(new Date());
                    service.save(reparatur);
                    erledigt++;
                }
            }
            JOptionPane.showMessageDialog(main,
                    "Es wurde/n " + erledigt + " Auftrag/Aufträge erledigt."
                            + (bereitsErledigt > 0
                                    ? " (" + bereitsErledigt + " der gewählten Aufträge waren bereits als erledigt markiert.)"
                                    : StringUtils.EMPTY));
        };
    }

    private JPanel createSuchePanel() {
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 140));
        panel.setLayout(new GridLayout(6, 1));
        panel.add(new JLabel("Abholdatum von"));
        panel.add(datumVonFeld);
        panel.add(new JLabel("Abholdatum bis"));
        panel.add(datumBisFeld);
        panel.add(new JLabel("Erledigte anzeigen"));
        panel.add(erledigteAnzeigenFeld);
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
                // do nothing
            }
        };
    }

}
