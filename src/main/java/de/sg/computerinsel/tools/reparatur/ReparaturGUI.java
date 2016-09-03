package de.sg.computerinsel.tools.reparatur;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import de.sg.computerinsel.tools.HibernateService;

public class ReparaturGUI {

    private final HibernateService service = new HibernateService();

    public void create(final JFrame main) {
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setTitle("Reparaturprogramm V1.0.0 © Sita Geßner");
    }
}
