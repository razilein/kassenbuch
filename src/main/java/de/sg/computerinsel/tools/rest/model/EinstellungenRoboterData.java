package de.sg.computerinsel.tools.rest.model;

import de.sg.computerinsel.tools.Einstellungen;
import lombok.Data;

@Data
public class EinstellungenRoboterData {

    Einstellungen roboterCron;

    Einstellungen roboterFiliale;

    Einstellungen roboterMailBodyReparaturauftrag;

    Einstellungen roboterMailBodyReparaturauftragGeraetErhalten;

    Einstellungen roboterEmail;

    Einstellungen roboterFtpUser;

    Einstellungen roboterFtpPassword;

}
