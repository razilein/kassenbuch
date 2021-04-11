package de.sg.computerinsel.tools.reparatur.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

import lombok.Data;

@Data
public class ReparaturCsvDto {

    public ReparaturCsvDto(final String dateiname, final String csv) {
        if (StringUtils.isBlank(csv)) {
            throw new IllegalStateException("Die CSV-Datei " + dateiname + " ist leer.");
        }
        final List<String> parts = new ArrayList<>(Splitter.on("|").splitToList(csv));
        if (parts.size() < 10) {
            throw new IllegalStateException("Die CSV-Datei " + dateiname + " ist unvollständig.");
        }
        this.nachname = getFirstPart(parts);
        this.vorname = getFirstPart(parts);
        this.strasse = getFirstPart(parts);
        this.plz = getFirstPart(parts);
        this.ort = getFirstPart(parts);
        this.email = getFirstPart(parts);
        this.telefon = getFirstPart(parts);
        this.geraet = getFirstPart(parts);
        this.geraetepasswort = getFirstPart(parts);
        this.symptome = getFirstPart(parts);
    }

    private String nachname;

    private String vorname;

    private String strasse;

    private String plz;

    private String ort;

    private String email;

    private String telefon;

    private String geraet;

    private String geraetepasswort;

    private String symptome;

    private String getFirstPart(final List<String> parts) {
        final String firstPart = parts.get(0);
        parts.remove(0);
        return firstPart;
    }

}
