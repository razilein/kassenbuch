package de.sg.computerinsel.tools;

import javax.swing.JFrame;

import de.sg.computerinsel.tools.kassenbuch.KassenbuchGUI;

/**
 * @author Sita Geßner TODO: <br>
 *         - Tagessumme in neuer Zeile abbilden mit Leerzeile dazwischen <br>
 *         - Einnahmen und Ausgaben trennen <br>
 *         - Nummerische Sortierung der Rechnungsnummern (nicht String-Sortierung)<br>
 *         - Anfangsbestand für nächsten Tag eintragen - Einnahmen, Ausgaben trennen - Kassenrechner (Anzahl Scheine, Münzen und
 *         Differenzbetrag zu Kassenbuch ermitteln)
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
