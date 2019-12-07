package de.sg.computerinsel.tools.einkauf.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import de.sg.computerinsel.tools.einkauf.dao.EinkaufRepository;
import de.sg.computerinsel.tools.einkauf.model.Einkauf;
import de.sg.computerinsel.tools.inventar.model.Produkt;
import de.sg.computerinsel.tools.rechnung.model.Rechnungsposten;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EinkaufService {

    private final EinkaufRepository einkaufRepository;

    public String getEinkaufslisteAsText() {
        final StringBuilder builder = new StringBuilder();
        Lists.newArrayList(einkaufRepository.findAll()).forEach(b -> {
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

    public void saveEinkaufsliste(final List<Rechnungsposten> rechnungsposten) {
        rechnungsposten.stream().filter(r -> !r.getProdukt().isBestandUnendlich()).forEach(r -> {
            final Produkt produkt = r.getProdukt();

            final Einkauf einkauf = einkaufRepository.findByProduktId(produkt.getId()).orElseGet(() -> createEinkauf(produkt));
            einkauf.setMenge(einkauf.getMenge() + r.getMenge());
            einkaufRepository.save(einkauf);
        });
    }

    private Einkauf createEinkauf(final Produkt produkt) {
        final Einkauf einkauf = new Einkauf();
        einkauf.setProdukt(produkt);
        return einkauf;
    }

    public void deleteAllEinkaeufe() {
        einkaufRepository.deleteAll();
    }

}
