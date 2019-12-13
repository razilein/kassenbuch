package de.sg.computerinsel.tools.kunde.service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import de.sg.computerinsel.tools.kunde.dao.KundeRepository;
import de.sg.computerinsel.tools.kunde.dao.VKundeRepository;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.model.KundeDuplikatDto;
import de.sg.computerinsel.tools.kunde.model.VKunde;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.service.FindAllByConditionsExecuter;
import de.sg.computerinsel.tools.service.SearchQueryUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KundeService {

    private final KundeRepository kundeRepository;

    private final VKundeRepository vKundeRepository;

    private final RechnungService rechnungService;

    private final ReparaturService reparaturService;

    private final PhoneNumberUtil phoneNumberUtil;

    public KundeService(final KundeRepository kundeRepository, final VKundeRepository vKundeRepository,
            final RechnungService rechnungService, final ReparaturService reparaturService) {
        this.kundeRepository = kundeRepository;
        this.vKundeRepository = vKundeRepository;
        this.rechnungService = rechnungService;
        this.reparaturService = reparaturService;
        phoneNumberUtil = PhoneNumberUtil.getInstance();
    }

    public Page<VKunde> listKunden(final PageRequest pagination, final Map<String, String> conditions) {
        final String vorname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "vorname");
        final String nachname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "nachname");
        final String firmenname = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "firmenname");
        final String plz = SearchQueryUtils.getAndReplaceOrAddJoker(conditions, "plz");
        final String telefon = createSuchfeldTelefon(conditions.get("telefon"));

        if (StringUtils.isBlank(vorname) && StringUtils.isBlank(nachname) && StringUtils.isBlank(firmenname) && StringUtils.isBlank(plz)
                && StringUtils.isBlank(telefon)) {
            return vKundeRepository.findAll(pagination);
        } else {
            final FindAllByConditionsExecuter<VKunde> executer = new FindAllByConditionsExecuter<>();
            return executer.findByParams(vKundeRepository, pagination,
                    buildMethodnameForQueryKunde(vorname, nachname, firmenname, plz, telefon), vorname, nachname, firmenname, plz, telefon);
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

    private String buildMethodnameForQueryKunde(final String vorname, final String nachname, final String firmenname, final String plz,
            final String telefon) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(vorname)) {
            methodName += "VornameLikeAnd";
        }
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "NachnameLikeAnd";
        }
        if (StringUtils.isNotBlank(firmenname)) {
            methodName += "FirmennameLikeAnd";
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
        if (kunde.getTelefon() != null) {
            kunde.setTelefon(formatTelefonnummer(kunde.getTelefon()));
        }
        kunde.setSuchfeldName(createSuchfeldName(kunde.getFirmenname(), kunde.getVorname(), kunde.getNachname()));
        return kundeRepository.save(kunde);
    }

    private String formatTelefonnummer(final String telefon) {
        String result = telefon;
        try {
            final PhoneNumber number = phoneNumberUtil.parseAndKeepRawInput(telefon, Locale.GERMANY.getLanguage().toUpperCase());
            if (phoneNumberUtil.isValidNumber(number)) {
                result = phoneNumberUtil.format(number, PhoneNumberFormat.NATIONAL);
            }
        } catch (final NumberParseException e) {
            log.debug("Telefonnummer: '{}' kann nicht formatiert werden.", telefon, e);
        }
        return result;
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
