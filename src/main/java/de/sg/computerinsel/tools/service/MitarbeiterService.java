package de.sg.computerinsel.tools.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.dao.MitarbeiterRepository;
import de.sg.computerinsel.tools.dao.MitarbeiterRolleRepository;
import de.sg.computerinsel.tools.dao.RolleRepository;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.MitarbeiterRolle;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.rest.ValidationUtils;
import de.sg.computerinsel.tools.rest.model.MitarbeiterDTO;
import de.sg.computerinsel.tools.rest.model.MitarbeiterRollenDTO;
import de.sg.computerinsel.tools.rest.model.RolleDTO;
import de.sg.computerinsel.tools.rest.model.UserDTO;
import lombok.AllArgsConstructor;

/**
 * @author Sita Geßner
 */
@Service
@AllArgsConstructor
public class MitarbeiterService {

    private final EinstellungenService einstellungenService;

    private final MitarbeiterRepository mitarbeiterRepository;

    private final MitarbeiterRolleRepository mitarbeiterRolleRepository;

    private final RolleRepository rolleRepository;

    public Optional<Mitarbeiter> getAngemeldeterMitarbeiter() {
        return mitarbeiterRepository.findByBenutzername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public String getAngemeldeterMitarbeiterVornameNachname() {
        final Optional<Mitarbeiter> optional = getAngemeldeterMitarbeiter();
        return optional.isPresent() ? optional.get().getCompleteNameReverse() : null;
    }

    public String getAngemeldeterMitarbeiterNachnameVorname() {
        final Optional<Mitarbeiter> optional = getAngemeldeterMitarbeiter();
        return optional.isPresent() ? optional.get().getCompleteName() : null;
    }

    public boolean checkPasswordBefore(final Mitarbeiter mitarbeiter, final String passwordBefore) {
        return BCrypt.checkpw(passwordBefore, mitarbeiter.getPasswort());
    }

    public Map<String, Object> saveMitarbeiterProfil(final MitarbeiterDTO dto, final Optional<Mitarbeiter> optional) {
        final Map<String, Object> result = new HashMap<>();
        if (optional.isPresent()) {
            final Mitarbeiter mitarbeiter = optional.get();
            mitarbeiter.setNachname(dto.getNachname());
            mitarbeiter.setVorname(dto.getVorname());
            mitarbeiter.setEmail(dto.getEmail());
            mitarbeiter.setEmailPrivat(dto.getEmailPrivat());
            mitarbeiter.setTelefon(dto.getTelefon());
            mitarbeiter.setFiliale(dto.getFiliale());

            ValidationUtils.validate(mitarbeiter);
            if (result.isEmpty()) {
                einstellungenService.save(mitarbeiter);
                result.put(Message.SUCCESS.getCode(), "Der Mitarbeiter '" + dto.getCompleteName() + "' wurde erfolgreich gespeichert");
            }
        } else {
            result.put(Message.ERROR.getCode(), "Ungültige Anmeldedaten.");
        }

        return result;
    }

    public Map<String, Object> saveAnmeldedaten(final UserDTO dto, final Optional<Mitarbeiter> optional) {
        final Map<String, Object> result = new HashMap<>();

        if (optional.isPresent()) {
            final Mitarbeiter mitarbeiter = optional.get();
            if (geaenderterBenutzernameExistiertBereits(dto, mitarbeiter)) {
                result.put(Message.ERROR.getCode(), "Der gewählte Benutzername ist bereits vergeben.");
            } else if (!StringUtils.isAllBlank(dto.getPassword(), dto.getPasswordRepeat())
                    && !checkPasswordBefore(mitarbeiter, dto.getPasswordBefore())) {
                result.put(Message.ERROR.getCode(), "Das alte Passwort stimmt nicht überein.");
            } else if (!StringUtils.isAllBlank(dto.getPassword(), dto.getPasswordRepeat()) && dto.getPassword().length() < 10) {
                result.put(Message.ERROR.getCode(), "Das Passwort darf nicht kürzer als 10 Zeichen sein.");
            } else if (dto.getUsername().length() < Mitarbeiter.MIN_LENGTH_BENUTZERNAME
                    || dto.getUsername().length() > Mitarbeiter.MAX_LENGTH_BENUTZERNAME) {
                result.put(Message.ERROR.getCode(), "Der Benutzername darf nicht kürzer als 6 und nicht länger als 50 Zeichen sein.");
            } else {
                saveUsernameAndPassword(dto, mitarbeiter);
                result.put(Message.SUCCESS.getCode(), "Die Anmeldedaten wurden geändert.");
            }
        } else {
            result.put(Message.ERROR.getCode(), "Ungültige Anmeldedaten.");
        }
        return result;
    }

    private void saveUsernameAndPassword(final UserDTO dto, final Mitarbeiter mitarbeiter) {
        final boolean changedUsername = !StringUtils.equals(dto.getUsername(), mitarbeiter.getBenutzername());
        if (changedUsername) {
            mitarbeiter.setBenutzername(dto.getUsername());
        }
        if (!StringUtils.isBlank(dto.getPassword())) {
            mitarbeiter.setPasswort(einstellungenService.hashPassword(dto.getPassword()));
        }
        einstellungenService.save(mitarbeiter);
        if (changedUsername) {
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        }
    }

    private boolean geaenderterBenutzernameExistiertBereits(final UserDTO dto, final Mitarbeiter mitarbeiter) {
        return !StringUtils.equals(dto.getUsername(), mitarbeiter.getBenutzername())
                && einstellungenService.existsUsername(dto.getUsername());
    }

    public MitarbeiterRollenDTO listRollen(final Integer mitarbeiterId) {
        final List<Integer> rollen = mitarbeiterRolleRepository.findByMitarbeiterId(mitarbeiterId).stream().map(mr -> mr.getRolle().getId())
                .collect(Collectors.toList());
        final List<RolleDTO> mitarbeiterRollen = einstellungenService.listRollen().stream()
                .map(r -> new RolleDTO(r.getId(), r.getBeschreibung(), rollen.contains(r.getId()))).collect(Collectors.toList());
        return new MitarbeiterRollenDTO(mitarbeiterId, mitarbeiterRollen);
    }

    @Transactional
    public void saveRollen(final MitarbeiterRollenDTO dto) {
        if (dto.getMitarbeiterId() != null) {
            mitarbeiterRepository.findById(dto.getMitarbeiterId()).ifPresent(m -> {
                mitarbeiterRolleRepository.deleteByMitarbeiterId(dto.getMitarbeiterId());
                saveRollen(dto, m);
            });
        }
    }

    private void saveRollen(final MitarbeiterRollenDTO dto, final Mitarbeiter mitarbeiter) {
        for (final RolleDTO rolle : dto.getRollen()) {
            if (rolle.isRight()) {
                rolleRepository.findById(rolle.getId())
                        .ifPresent(r -> mitarbeiterRolleRepository.save(new MitarbeiterRolle(mitarbeiter, r)));

            }
        }
    }

}
