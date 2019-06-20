package de.sg.computerinsel.tools.kassenbuch.rest.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Sita Geßner
 */
@Data
public class KassenbuchStatistik {

    @NotNull(message = "kassenbuch.statistik.zeitraum.von.error")
    private Date zeitraumVon;

    @NotNull(message = "kassenbuch.statistik.zeitraum.bis.error")
    private Date zeitraumBis;

    private String posten;

}
