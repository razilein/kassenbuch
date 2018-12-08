package de.sg.computerinsel.tools.rest;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.model.MitarbeiterDTO;
import de.sg.computerinsel.tools.rest.model.UserDTO;
import de.sg.computerinsel.tools.service.MitarbeiterService;

@RestController
@RequestMapping("/mitarbeiter-profil")
public class MitarbeiterProfilRestController {

    @Autowired
    private MitarbeiterService service;

    @GetMapping
    public MitarbeiterDTO get() {
        return new MitarbeiterDTO(service.getAngemeldeterMitarbeiter().orElse(null));
    }

    @GetMapping("/anmeldedaten")
    public UserDTO benutzername() {
        final UserDTO dto = new UserDTO();
        dto.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return dto;
    }

    @PutMapping
    public Map<String, Object> save(@RequestBody final MitarbeiterDTO dto) {
        final Optional<Mitarbeiter> optional = service.getAngemeldeterMitarbeiter();
        return service.saveMitarbeiterProfil(dto, optional);
    }

    @PutMapping("/anmeldedaten")
    public Map<String, Object> saveAnmeldedaten(@RequestBody final UserDTO dto) {
        final Optional<Mitarbeiter> optional = service.getAngemeldeterMitarbeiter();
        return service.saveAnmeldedaten(dto, optional);
    }

}
