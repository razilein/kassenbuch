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
            new KassenbuchGUI().create(main);
        } else {
            new ReparaturGUI().create(main);
        }
        main.setSize(600, 500);
        main.setResizable(false);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}
