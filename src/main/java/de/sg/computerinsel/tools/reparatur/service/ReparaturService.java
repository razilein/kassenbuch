package de.sg.computerinsel.tools.reparatur.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.reparatur.dao.KundeRepository;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReparaturService {

    private final KundeRepository kundeRepository;

    public Page<Kunde> listKunden(final PageRequest pagination, final Map<String, String> conditions) {
        final String nachname = getAndReplaceJoker(conditions, "nachname");
        final String vorname = getAndReplaceJoker(conditions, "vorname");
        final String plz = getAndReplaceJoker(conditions, "plz");

        if (StringUtils.isBlank(nachname) && StringUtils.isBlank(vorname) && StringUtils.isBlank(plz)) {
            return kundeRepository.findAll(pagination);
        } else {
            return findByParams(pagination, nachname, vorname, plz);
        }
    }

    @SuppressWarnings("unchecked")
    private Page<Kunde> findByParams(final PageRequest pagination, final String nachname, final String vorname, final String plz) {
        final String methodname = buildMethodnameForQuery(nachname, vorname, plz);
        final List<String> params = getMethodParams(nachname, vorname, plz);
        try {
            Method method;
            if (params.size() == 1) {
                method = KundeRepository.class.getDeclaredMethod(methodname, String.class, Pageable.class);
                return (Page<Kunde>) method.invoke(kundeRepository, params.get(0), pagination);
            } else if (params.size() == 2) {
                method = KundeRepository.class.getDeclaredMethod(methodname, String.class, String.class, Pageable.class);
                return (Page<Kunde>) method.invoke(kundeRepository, params.get(0), params.get(1), pagination);
            } else {
                method = KundeRepository.class.getDeclaredMethod(methodname, String.class, String.class, String.class, Pageable.class);
                return (Page<Kunde>) method.invoke(kundeRepository, params.get(0), params.get(1), params.get(2), pagination);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<String> getMethodParams(final String... felder) {
        return Arrays.asList(felder).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    private String buildMethodnameForQuery(final String nachname, final String vorname, final String plz) {
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

    public void save(final Kunde kunde) {
        kundeRepository.save(kunde);
    }

    public void deleteKunde(final Integer id) {
        kundeRepository.deleteById(id);
    }

}
