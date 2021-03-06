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

import de.sg.computerinsel.tools.dao.FilialeRepository;
import de.sg.computerinsel.tools.dao.MitarbeiterRepository;
import de.sg.computerinsel.tools.dao.MitarbeiterRolleRepository;
import de.sg.computerinsel.tools.dao.RolleRepository;
import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter.MitarbeiterAnmeldedaten;
import de.sg.computerinsel.tools.reparatur.model.MitarbeiterRolle;
import de.sg.computerinsel.tools.rest.Message;
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

    private final FilialeRepository filialeRepository;

    private final MessageService messageService;

    private final MitarbeiterRepository mitarbeiterRepository;

    private final MitarbeiterRolleRepository mitarbeiterRolleRepository;

    private final RolleRepository rolleRepository;

    private final ValidationService validationService;

    public Optional<Mitarbeiter> getAngemeldeterMitarbeiter() {
        return mitarbeiterRepository.findByBenutzername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public Optional<Filiale> getAngemeldeterMitarbeiterFiliale() {
        final Optional<Mitarbeiter> optional = getAngemeldeterMitarbeiter();
        if (optional.isPresent()) {
            return filialeRepository.findById(optional.get().getFiliale().getId());
        }
        return Optional.empty();
    }

    public String getAndSaveNextAngebotsnummer() {
        final Optional<Filiale> optional = getAngemeldeterMitarbeiterFiliale();
        if (optional.isPresent()) {
            final Filiale filiale = optional.get();
            filiale.setZaehlerAngebot(filiale.getZaehlerAngebot() + 1);
            einstellungenService.save(filiale);
            return StringUtils.leftPad(String.valueOf(filiale.getZaehlerAngebot()), 4, "0");
        }
        return "0";
    }

    public String getAndSaveNextBestellnummer() {
        final Optional<Filiale> optional = getAngemeldeterMitarbeiterFiliale();
        if (optional.isPresent()) {
            final Filiale filiale = optional.get();
            filiale.setZaehlerBestellung(filiale.getZaehlerBestellung() + 1);
            einstellungenService.save(filiale);
            return StringUtils.leftPad(String.valueOf(filiale.getZaehlerBestellung()), 4, "0");
        }
        return "0";
    }

    public String getAndSaveNextRechnungsnummer() {
        final Optional<Filiale> optional = getAngemeldeterMitarbeiterFiliale();
        if (optional.isPresent()) {
            final Filiale filiale = optional.get();
            filiale.setZaehlerRechnung(filiale.getZaehlerRechnung() + 1);
            einstellungenService.save(filiale);
            return StringUtils.leftPad(String.valueOf(filiale.getZaehlerRechnung()), 4, "0");
        }
        return "0";
    }

    public String getAndSaveNextReparaturnummer() {
        final Optional<Filiale> optional = getAngemeldeterMitarbeiterFiliale();
        return optional.isPresent() ? getAndSaveNextReparaturnummer(optional.get()) : "0";
    }

    public String getAndSaveNextReparaturnummer(final Filiale filiale) {
        filiale.setZaehlerReparaturauftrag(filiale.getZaehlerReparaturauftrag() + 1);
        einstellungenService.save(filiale);
        return StringUtils.leftPad(String.valueOf(filiale.getZaehlerReparaturauftrag()), 4, "0");
    }

    public String getAndSaveNextStornierungnummer() {
        final Optional<Filiale> optional = getAngemeldeterMitarbeiterFiliale();
        if (optional.isPresent()) {
            final Filiale filiale = optional.get();
            filiale.setZaehlerStornierung(filiale.getZaehlerStornierung() + 1);
            einstellungenService.save(filiale);
            return StringUtils.leftPad(String.valueOf(filiale.getZaehlerStornierung()), 4, "0");
        }
        return "0";
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
            mitarbeiter.setDruckansichtNeuesFenster(dto.isDruckansichtNeuesFenster());
            mitarbeiter.setDruckansichtDruckdialog(dto.isDruckansichtDruckdialog());

            if (mitarbeiter.getId() == null) {
                result.putAll(validationService.validate(mitarbeiter));
            } else {
                result.putAll(validationService.validate(mitarbeiter, MitarbeiterAnmeldedaten.class));
            }

            if (result.isEmpty()) {
                einstellungenService.save(mitarbeiter);
                result.put(Message.SUCCESS.getCode(), messageService.get("einstellungen.mitarbeiter.save.success", dto.getCompleteName()));
            }
        } else {
            result.put(Message.ERROR.getCode(), messageService.get("einstellungen.mitarbeiter.save.error"));
        }

        return result;
    }

    public Map<String, Object> saveAnmeldedaten(final UserDTO dto, final Optional<Mitarbeiter> optional) {
        final Map<String, Object> result = new HashMap<>();

        if (optional.isPresent()) {
            final Mitarbeiter mitarbeiter = optional.get();
            if (geaenderterBenutzernameExistiertBereits(dto, mitarbeiter)) {
                result.put(Message.ERROR.getCode(), messageService.get("einstellungen.mitarbeiter.benutzername.error.used"));
            } else if (!StringUtils.isAllBlank(dto.getPassword(), dto.getPasswordRepeat())
                    && !checkPasswordBefore(mitarbeiter, dto.getPasswordBefore())) {
                result.put(Message.ERROR.getCode(), messageService.get("einstellungen.mitarbeiter.psw.error.wrong"));
            } else if (!StringUtils.isAllBlank(dto.getPassword(), dto.getPasswordRepeat()) && dto.getPassword().length() < 10) {
                result.put(Message.ERROR.getCode(), messageService.get("einstellungen.mitarbeiter.psw.error.size"));
            } else if (dto.getUsername().length() < Mitarbeiter.MIN_LENGTH_BENUTZERNAME
                    || dto.getUsername().length() > Mitarbeiter.MAX_LENGTH_BENUTZERNAME) {
                result.put(Message.ERROR.getCode(), messageService.get("einstellungen.mitarbeiter.benutzername.error.size"));
            } else {
                saveUsernameAndPassword(dto, mitarbeiter);
                result.put(Message.SUCCESS.getCode(), messageService.get("einstellungen.mitarbeiter.psw.save.success"));
            }
        } else {
            result.put(Message.ERROR.getCode(), messageService.get("einstellungen.mitarbeiter.save.error"));
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
