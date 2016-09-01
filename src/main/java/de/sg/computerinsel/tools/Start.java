package de.sg.computerinsel.tools;

import javax.swing.JFrame;

import de.sg.computerinsel.tools.kassenbuch.KassenbuchGUI;

/**
 * @author Sita Geßner
 */
public class Start {

    public static void main(final String[] args) {
        final JFrame main = new JFrame();
        new KassenbuchGUI().create(main);
        main.setSize(600, 500);
        main.setResizable(false);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}
