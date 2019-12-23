package de.sg.computerinsel.tools.reparatur.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeraetepasswortArt {

    PASSWORT(0, "Gerätepasswort"), KEIN_PASSWORT(1, "Kein Gerätepasswort"), MUSTER(2, "Mustersperre");

    private final int code;

    private final String description;

}
