package de.sg.computerinsel.tools.kassenbuch.rest.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
public class KassenbuchStatistik {

    @NotNull(message = "Bitte geben Sie den Rechnungszeitraum von an.")
    private Date zeitraumVon;

    @NotNull(message = "Bitte geben Sie den Rechnungszeitraum bis an.")
    private Date zeitraumBis;

    private String posten;

}
