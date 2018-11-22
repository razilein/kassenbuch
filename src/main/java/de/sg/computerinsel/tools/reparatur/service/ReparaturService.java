package de.sg.computerinsel.tools.reparatur.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.primitives.Ints;

import de.sg.computerinsel.tools.reparatur.dao.KundeRepository;
import de.sg.computerinsel.tools.reparatur.dao.ReparaturRepository;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.model.ReparaturArt;
import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReparaturService {

    private final EinstellungenService einstellungenService;

    private final KundeRepository kundeRepository;

    private final ReparaturRepository reparaturRepository;

    @SuppressWarnings("unchecked")
    public Page<Kunde> listKunden(final PageRequest pagination, final Map<String, String> conditions) {
        final String nachname = getAndReplaceJoker(conditions, "nachname");
        final String vorname = getAndReplaceJoker(conditions, "vorname");
        final String plz = getAndReplaceJoker(conditions, "plz");

        if (StringUtils.isBlank(nachname) && StringUtils.isBlank(vorname) && StringUtils.isBlank(plz)) {
            return kundeRepository.findAll(pagination);
        } else {
            return (Page<Kunde>) findByParams(kundeRepository, pagination, buildMethodnameForQueryKunde(nachname, vorname, plz), nachname,
                    vorname, plz);
        }
    }

    @SuppressWarnings("rawtypes")
    private Page<?> findByParams(final CrudRepository repository, final PageRequest pagination, final String methodname,
            final String... felder) {
        final List<String> params = getMethodParams(felder);
        try {
            Method method;
            if (params.size() == 1) {
                method = repository.getClass().getDeclaredMethod(methodname, String.class, Pageable.class);
                return (Page<?>) method.invoke(repository, params.get(0), pagination);
            } else if (params.size() == 2) {
                method = repository.getClass().getDeclaredMethod(methodname, String.class, String.class, Pageable.class);
                return (Page<?>) method.invoke(repository, params.get(0), params.get(1), pagination);
            } else {
                method = repository.getClass().getDeclaredMethod(methodname, String.class, String.class, String.class, Pageable.class);
                return (Page<?>) method.invoke(repository, params.get(0), params.get(1), params.get(2), pagination);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<String> getMethodParams(final String... felder) {
        return Arrays.asList(felder).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    private String buildMethodnameForQueryKunde(final String nachname, final String vorname, final String plz) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "NachnameLikeAnd";
        }
        if (StringUtils.isNotBlank(vorname)) {
            methodName += "VornameLikeAnd";
        }
        if (StringUtils.isNotBlank(plz)) {
            methodName += "PlzLikeAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    private static String getAndReplaceJoker(final Map<String, String> conditions, final String key) {
        return StringUtils.replace(conditions.get(key), "*", "%");
    }

    public Optional<Kunde> getKunde(final Integer id) {
        return kundeRepository.findById(id);
    }

    public Kunde save(final Kunde kunde) {
        return kundeRepository.save(kunde);
    }

    public void deleteKunde(final Integer id) {
        kundeRepository.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public Page<Reparatur> listReparaturen(final PageRequest pagination, final Map<String, String> conditions) {
        final String nachname = getAndReplaceJoker(conditions, "nachname");
        final String nummer = getAndReplaceJoker(conditions, "nummer");
        final String kundeId = getAndReplaceJoker(conditions, "kunde.id");

        if (StringUtils.isNumeric(kundeId)) {
            return reparaturRepository.findByKundeId(Ints.tryParse(kundeId), pagination);
        } else if (StringUtils.isBlank(nachname) && StringUtils.isBlank(nummer)) {
            return reparaturRepository.findAll(pagination);
        } else {
            return (Page<Reparatur>) findByParams(reparaturRepository, pagination, buildMethodnameForQueryReparatur(nachname, nummer),
                    nachname, nummer);
        }
    }

    private String buildMethodnameForQueryReparatur(final String nachname, final String nummer) {
        String methodName = "findBy";
        if (StringUtils.isNotBlank(nachname)) {
            methodName += "KundeNachnameLikeAnd";
        }
        if (StringUtils.isNotBlank(nummer)) {
            methodName += "NummerLikeAnd";
        }
        return StringUtils.removeEnd(methodName, "And");
    }

    public Optional<Reparatur> getReparatur(final Integer id) {
        return reparaturRepository.findById(id);
    }

    public List<DefaultKeyValue> getReparaturarten() {
        return Arrays.asList(ReparaturArt.values()).stream().map(r -> new DefaultKeyValue(r.getCode(), r.getDescription()))
                .collect(Collectors.toList());
    }

    public Reparatur save(final Reparatur reparatur) {
        if (StringUtils.isBlank(reparatur.getNummer())) {
            final String id = einstellungenService.getFiliale().getWert();
            final Optional<Filiale> filiale = einstellungenService.getFiliale(id == null ? null : Ints.tryParse(id));

            final String nummer = StringUtils.leftPad(String.valueOf(reparaturRepository.getNextAuftragsnummer()), 4, "0");
            if (filiale.isPresent()) {
                reparatur.setNummer(filiale.get().getKuerzel());
            }
            reparatur.setNummer(reparatur.getNummer() + nummer);
        }
        return reparaturRepository.save(reparatur);
    }

    public void deleteReparatur(final Integer id) {
        reparaturRepository.deleteById(id);
    }

}
