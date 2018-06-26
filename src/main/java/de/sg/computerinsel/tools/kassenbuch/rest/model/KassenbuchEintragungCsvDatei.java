package de.sg.computerinsel.tools.kassenbuch.rest.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
public class KassenbuchEintragungCsvDatei {

    private String csvDatei;

    private List<KassenbuchEintragungManuell> eintragungen = new ArrayList<>();

}
