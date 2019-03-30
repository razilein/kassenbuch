package de.sg.computerinsel.tools.bestellung.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import de.sg.computerinsel.tools.bestellung.dao.BestellungRepository;
import de.sg.computerinsel.tools.bestellung.model.Bestellung;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BestellungService {

    private final BestellungRepository bestellungRepository;

    public String getBestellungenAsText() {
        final StringBuilder builder = new StringBuilder();
        Lists.newArrayList(bestellungRepository.findAll()).forEach(b -> {
            builder.append(b.getMenge());
            builder.append(" x ");
            if (StringUtils.isNotBlank(b.getProdukt().getEan())) {
                builder.append(b.getProdukt().getEan());
                builder.append(StringUtils.SPACE);
            }
            builder.append(b.getProdukt().getBezeichnung());
            builder.append(System.lineSeparator());
        });
        return builder.toString();
    }

    public void saveBestellung(final List<Rechnungsposten> rechnungsposten) {
        rechnungsposten.stream().filter(r -> !r.getProdukt().isBestandUnendlich()).forEach(r -> {
            final Produkt produkt = r.getProdukt();

            final Bestellung bestellung = bestellungRepository.findByProduktId(produkt.getId()).orElseGet(() -> createBestellung(produkt));
            bestellung.setMenge(bestellung.getMenge() + r.getMenge());
            bestellungRepository.save(bestellung);
        });
    }

    private Bestellung createBestellung(final Produkt produkt) {
        final Bestellung bestellung = new Bestellung();
        bestellung.setProdukt(produkt);
        return bestellung;
    }

    public void deleteAllBestellungen() {
        bestellungRepository.deleteAll();
    }

}
