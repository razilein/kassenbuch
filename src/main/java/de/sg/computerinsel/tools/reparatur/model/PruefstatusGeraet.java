package de.sg.computerinsel.tools.reparatur.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PruefstatusGeraet {

    NICHT_DEFINIERT(-1, "Bitte auswählen"), FUNKTIONSFAEHIG(1, "Gerät funktioniert"), NICHT_FUNKTIONSFAEHIG(0,
            "Gerät funktioniert nicht"), NICHT_PRUEFBAR(2, "Gerät nicht prüfbar");

    private final int code;

    private final String description;

}
