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

    Einstellungen filiale;

    Einstellungen ftpHost;

    Einstellungen ftpPort;

    Einstellungen ftpUser;

    Einstellungen ftpPassword;

    Einstellungen rechnungsnummer;

    Einstellungen reparaturnummer;

}
