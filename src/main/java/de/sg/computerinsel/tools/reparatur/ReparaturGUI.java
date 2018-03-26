package de.sg.computerinsel.tools.reparatur;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.sg.computerinsel.tools.HibernateService;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;

public class ReparaturGUI {

    private final HibernateService service = new HibernateService();

    private final JMenu submenuFilialeWaehlen = new JMenu("Auswählen");

    private ButtonGroup btnGroup;

    public void create(final JFrame main) {
        main.setIconImage(new ImageIcon(getClass().getResource("pictures/zahnrad.png")).getImage());
        main.setTitle("Reparaturprogramm V1.0.0 © Sita Geßner");
        main.setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenuKunden());
        menuBar.add(createMenuEinstellungen());
        return menuBar;
    }

    private JMenu createMenuEinstellungen() {
        final JMenu menu = new JMenu("Einstellungen");
        menu.add(createMenuFilialie());
        menu.add(createMenuMitarbeiter());
        return menu;
    }

    private JMenu createMenuFilialie() {
        final JMenu menu = new JMenu("Filiale");
        submenuFilialeWaehlen.addMouseListener(getMouseListenerSubmenuFilialeWaehlen());
        menu.add(submenuFilialeWaehlen);
        menu.add(createMenuFilialienBearbeiten());
        return menu;
    }

    private MouseListener getMouseListenerSubmenuFilialeWaehlen() {
        return new MouseListener() {

            @Override
            public void mouseReleased(final MouseEvent e) {
            }

            @Override
            public void mousePressed(final MouseEvent e) {
            }

            @Override
            public void mouseExited(final MouseEvent e) {
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                createMenuFilialienWaehlen();
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                createMenuFilialienWaehlen();
            }
        };
    }

    private void createMenuFilialienWaehlen() {
        submenuFilialeWaehlen.removeAll();
        final List<IntegerBaseObject> list = service.list(Filiale.class);
        btnGroup = new ButtonGroup();
        for (int i = 0; i < list.size(); i++) {
            final IntegerBaseObject obj = list.get(i);
            if (obj instanceof Filiale) {
                final Filiale filiale = (Filiale) obj;
                final JRadioButtonMenuItem item = new JRadioButtonMenuItem(filiale.toHtmlString());
                item.setSelected(filiale.getId().equals(SettingsUtils.getFiliale()));
                item.addActionListener(getActionListenerRadioBtnFilalie(item, filiale.getId()));
                btnGroup.add(item);
                submenuFilialeWaehlen.add(item);
                if (i < list.size() - 1) {
                    submenuFilialeWaehlen.addSeparator();
                }
            }
        }
    }

    private ActionListener getActionListenerRadioBtnFilalie(final JRadioButtonMenuItem item, final Integer id) {
        return e -> {
            SettingsUtils.saveFiliale(id);
            item.setSelected(true);
        };
    }

    private JMenuItem createMenuFilialienBearbeiten() {
        final JMenuItem menu = new JMenuItem("Bearbeiten");
        menu.addActionListener(e -> {
            new FilialeGUI(service);
        });
        return menu;
    }

    private JMenuItem createMenuMitarbeiter() {
        final JMenu menu = new JMenu("Mitarbeiter");
        menu.add(createMenuMitarbeiterBearbeiten());
        return menu;
    }

    private JMenuItem createMenuMitarbeiterBearbeiten() {
        final JMenuItem menu = new JMenuItem("Bearbeiten");
        menu.addActionListener(e -> {
            new MitarbeiterGUI(service);
        });
        return menu;
    }

    private JMenuItem createMenuKunden() {
        final JMenu menu = new JMenu("Kunden");
        menu.add(createMenuKundenBearbeiten());
        menu.add(createMenuBericht());
        return menu;
    }

    private JMenuItem createMenuKundenBearbeiten() {
        final JMenuItem menu = new JMenuItem("Liste");
        menu.setToolTipText("Hier können Sie Kundenkarteien durchsuchen/erstellen/bearbeiten/löschen und die Reparaturen zu einem Kunden aufrufen.");
        menu.addActionListener(e -> {
            new KundenGUI(service);
        });
        return menu;
    }

    private JMenuItem createMenuBericht() {
        final JMenuItem menu = new JMenuItem("Was geht?");
        menu.setToolTipText("Hier können Sie einsehen welche Aufträge in einem bestimmten Zeitraum anstehen. Die Aufträge können erledigt werden.");
        menu.addActionListener(e -> {
            new BerichteGUI(service);
        });
        return menu;
    }
}
