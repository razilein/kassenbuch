package de.sg.computerinsel.tools;

import javax.swing.JFrame;

import de.sg.computerinsel.tools.kassenbuch.KassenbuchGUI;
import de.sg.computerinsel.tools.reparatur.ReparaturGUI;

/**
 * @author Sita Geßner
 */
public class Start {

    public static void main(final String[] args) {
        Tool tool = Tool.KASSENBUCH;
        if (args != null && args.length > 0) {
            tool = Tool.getByName(args[0]);
        }
        final JFrame main = new JFrame();
        if (Tool.KASSENBUCH == tool) {
            main.setSize(600, 500);
            new KassenbuchGUI().create(main);
        } else {
            main.setSize(700, 600);
            new ReparaturGUI().create(main);
        }
        main.setResizable(false);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}
