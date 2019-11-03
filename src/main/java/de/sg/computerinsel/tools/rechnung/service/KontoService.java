package de.sg.computerinsel.tools.rechnung.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.rechnung.model.Zahlart;
import de.sg.computerinsel.tools.reparatur.dao.FilialeKontenRepository;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.FilialeKonten;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Service
public class KontoService {

    private final FilialeKontenRepository filialeKontenRepository;

    public Integer getRechnungHaben(final Zahlart art, final FilialeKonten konten) {
        Integer result;
        switch (art) {
        case BAR:
            result = konten.getHabenBar();
            break;
        case EC:
            result = konten.getHabenEc();
            break;
        case PAYPAL:
            result = konten.getHabenPaypal();
            break;
        case UEBERWEISUNG:
            result = konten.getHabenUeberweisung();
            break;
        default:
            result = null;
            break;
        }
        return result;
    }

    public Integer getRechnungSoll(final Zahlart art, final FilialeKonten konten) {
        Integer result;
        switch (art) {
        case BAR:
            result = konten.getSollBar();
            break;
        case EC:
            result = konten.getSollEc();
            break;
        case PAYPAL:
            result = konten.getSollPaypal();
            break;
        case UEBERWEISUNG:
            result = konten.getSollUeberweisung();
            break;
        default:
            result = null;
            break;
        }
        return result;
    }

    public Map<Filiale, FilialeKonten> getKontenJeFiliale() {
        final List<FilialeKonten> konten = filialeKontenRepository.findAll();
        return konten.stream().collect(Collectors.toMap(FilialeKonten::getFiliale, k -> k));
    }

}
