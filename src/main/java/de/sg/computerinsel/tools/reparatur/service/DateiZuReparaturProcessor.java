package de.sg.computerinsel.tools.reparatur.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.model.VKunde;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.reparatur.model.BaseReparatur;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DateiZuReparaturProcessor implements Processor {

    private final EinstellungenService einstellungenService;

    private final KundeService kundeService;

    private final MitarbeiterService mitarbeiterService;

    private final ReparaturService reparaturService;

    @Override
    public void process(final Exchange exchange) throws Exception {
        final File datei = exchange.getIn().getBody(File.class);
        final String inhalt = FileUtils.readFileToString(datei, StandardCharsets.UTF_8);
        final ReparaturCsvDto dto = new ReparaturCsvDto(datei.getName(), inhalt);
        final Page<VKunde> kunden = kundeService.listKunden(PageRequest.of(0, 2), conditionsKunde(dto));

        final Filiale filiale = einstellungenService.getFiliale(Ints.tryParse(einstellungenService.getRoboterFiliale().getWert()))
                .orElseThrow(() -> new IllegalStateException("Filiale für Roboter in Einstellungen nicht gesetzt!"));

        final Kunde kunde;
        if (kunden.getSize() == 1) {
            kunde = kunden.getContent().stream().findFirst().map(VKunde::getId).map(kundeService::getKunde).filter(Optional::isPresent)
                    .map(Optional::get).orElseGet(() -> kundeService.save(new Kunde(dto)));
        } else {
            kunde = kundeService.save(new Kunde(dto));
        }

        final Reparatur reparatur = reparaturService.createReparatur();
        reparatur.setKunde(kunde);
        reparatur.setBestellung(null);
        reparatur.setFiliale(filiale);
        reparatur.setGeraet(StringUtils.abbreviate(dto.getGeraet(), BaseReparatur.LENGTH_GERAET));
        reparatur.setGeraetepasswort(StringUtils.abbreviate(dto.getGeraetepasswort(), BaseReparatur.LENGTH_GERAETEPASSWORT));
        reparatur.setSymptome(StringUtils.abbreviate(dto.getSymptome(), BaseReparatur.LENGTH_SYMPTOME));
        reparatur.setMitarbeiter("Roboter");
        reparatur.setKostenvoranschlag("Automatisch erzeugt - Bitte nachtragen");
        reparatur.setNummer(reparaturService.getReparaturJahrZweistellig() + mitarbeiterService.getAndSaveNextReparaturnummer(filiale));
        reparatur.setErstelltAm(LocalDateTime.now());
        reparatur.setNeu(true);
        exchange.getMessage().setBody(reparatur);
    }

    private Map<String, String> conditionsKunde(final ReparaturCsvDto dto) {
        final Map<String, String> conditions = new HashMap<>();
        conditions.put("vorname", dto.getVorname());
        conditions.put("nachname", dto.getNachname());
        conditions.put("plz", dto.getPlz());
        conditions.put("strasse", dto.getStrasse());
        return conditions;
    }

}
