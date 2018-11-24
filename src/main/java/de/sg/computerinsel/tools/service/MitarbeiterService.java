package de.sg.computerinsel.tools.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.dao.MitarbeiterRepository;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import lombok.AllArgsConstructor;

/**
 * @author Sita Geßner
 */
@Service
@AllArgsConstructor
public class MitarbeiterService {

    private final MitarbeiterRepository mitarbeiterRepository;

    public Optional<Mitarbeiter> getAngemeldeterMitarbeiter() {
        return mitarbeiterRepository.findByBenutzername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
