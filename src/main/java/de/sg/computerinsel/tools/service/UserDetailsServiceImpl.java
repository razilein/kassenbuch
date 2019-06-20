package de.sg.computerinsel.tools.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.sg.computerinsel.tools.dao.MitarbeiterRepository;
import de.sg.computerinsel.tools.dao.MitarbeiterRolleRepository;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.MitarbeiterRolle;
import de.sg.computerinsel.tools.reparatur.model.Rolle;
import lombok.AllArgsConstructor;

/**
 * @author Sita Geßner
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MitarbeiterRepository mitarbeiterRepository;

    private final MitarbeiterRolleRepository mitarbeiterRolleRepository;

    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        final Optional<Mitarbeiter> mitarbeiter = mitarbeiterRepository.findByBenutzername(username);

        if (!mitarbeiter.isPresent()) {
            throw new IllegalStateException(messageService.get("login.username.error", username));
        }

        final Mitarbeiter m = mitarbeiter.get();
        final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (final Rolle rolle : mitarbeiterRolleRepository.findByMitarbeiterId(m.getId()).stream().map(MitarbeiterRolle::getRolle)
                .collect(Collectors.toList())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(rolle.getName()));
        }

        return new User(m.getBenutzername(), m.getPasswort(), grantedAuthorities);
    }

}
