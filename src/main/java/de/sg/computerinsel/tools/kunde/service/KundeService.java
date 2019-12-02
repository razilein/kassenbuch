package de.sg.computerinsel.tools.kunde.service;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.sg.computerinsel.tools.kunde.dao.KundeRepository;
import de.sg.computerinsel.tools.kunde.dao.VKundeRepository;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.model.KundeDuplikatDto;
import de.sg.computerinsel.tools.kunde.model.VKunde;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KundeService {

    private final KundeRepository kundeRepository;

    private final VKundeRepository vKundeRepository;

    private final RechnungService rechnungService;

    private final ReparaturService reparaturService;

    public Page<VKunde> listKunden(final PageRequest pagination, final Map<String, String> conditions) {
        String name = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "suchfeld_name");
        name = RegExUtils.replaceAll(name, StringUtils.SPACE, "%");
        final String plz = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "plz");
        final String telefon = createSuchfeldTelefon(conditions.get("telefon"));

        if (StringUtils.isBlank(name) && StringUtils.isBlank(plz) && StringUtils.isBlank(telefon)) {
            return vKundeRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<VKunde> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vKundeRepository, pagination, buildMethodnameForQueryKunde(name, plz, telefon), name, plz,
                    telefon);
        }
    }

    private static String createSuchfeldTelefon(final String telefon) {
        String tel = StringUtils.trimToNull(telefon);
        if (StringUtils.isNotBlank(tel)) {
            tel = RegExUtils.replaceAll(tel, StringUtils.SPACE, StringUtils.EMPTY);
            tel = RegExUtils.replaceAll(tel, "\\*", StringUtils.EMPTY);
            tel = RegExUtils.replaceAll(tel, "/", StringUtils.EMPTY);
            tel = RegExUtils.replaceAll(tel, "\\\\", StringUtils.EMPTY);
            tel = RegExUtils.replaceAll(tel, "-", StringUtils.EMPTY);
            tel = RegExUtils.replaceAll(tel, StringUtils.EMPTY, "%");
        }
        return tel;
    }

    private String buildMethodnameForQueryKunde(final String name, final String plz, final String telefon) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(name)) {
            methodName += "SuchfeldNameLikeAnd";
        }
        if (StringUtils.isNotBlank(plz)) {
            methodName += "PlzLikeAnd";
        }
        if (StringUtils.isNotBlank(telefon)) {
            methodName += "TelefonLikeAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public Optional<Kunde> getKunde(final Integer id) {
        return kundeRepository.findById(id);
    }

    public void saveDsgvo(final Integer id) {
        getKunde(id).ifPresent(k -> {
            k.setDsgvo(true);
            save(k);
        });
    }

    public Kunde save(final Kunde kunde) {
        if (kunde.getNummer() == null) {
            kunde.setNummer(kundeRepository.getNextNummer());
        }
        kunde.setSuchfeldName(createSuchfeldName(kunde.getFirmenname(), kunde.getVorname(), kunde.getNachname()));
        return kundeRepository.save(kunde);
    }

    private static String createSuchfeldName(final String firmenname, final String vorname, final String nachname) {
        return RegExUtils.removeAll(
                StringUtils.trimToEmpty(firmenname).concat(StringUtils.trimToEmpty(vorname)).concat(StringUtils.trimToEmpty(nachname)),
                StringUtils.SPACE);
    }

    public void deleteKunde(final Integer id) {
        kundeRepository.deleteById(id);
    }

    @Transactional
    public void duplikatZusammenfuehren(final KundeDuplikatDto dto) {
        rechnungService.duplikateZusammenfuehren(dto);
        reparaturService.duplikateZusammenfuehren(dto);
        deleteKunde(dto.getDuplikat().getId());
    }

}
