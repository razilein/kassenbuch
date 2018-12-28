package de.sg.computerinsel.tools.rest.model;

import de.sg.computerinsel.tools.Einstellungen;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Sita Geßner
 */
@Getter
@Setter
public class EinstellungenData {

    Einstellungen ablageverzeichnis;

    Einstellungen rechnungsverzeichnis;

    Einstellungen filiale;

    Einstellungen rechnungsnummer;

    Einstellungen reparaturnummer;

}
