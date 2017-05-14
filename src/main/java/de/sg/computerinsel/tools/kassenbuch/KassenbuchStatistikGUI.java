package de.sg.computerinsel.tools.kassenbuch;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.sg.computerinsel.tools.kassenbuch.model.Einstellungen;

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
        final JButton btnEinlesen = new JButton("Rechnungen einlesen");
        btnEinlesen.addActionListener(getActionListenerBtnRechnungenEinlesen());
        final JButton btnAnzeigen = new JButton("Statistik anzeigen");
        btnAnzeigen.addActionListener(getActionListenerBtnStatistikAnzeigen());

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

        final Panel btnPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(btnEinlesen);
        btnPanel.add(btnAnzeigen);
        panel.add(btnPanel);

        return panel;
    }

    private ActionListener getActionListenerBtnStatistikAnzeigen() {
        return e -> {

        };
    }

    private ActionListener getActionListenerBtnRechnungenEinlesen() {
        return e -> {

        };
    }

}
