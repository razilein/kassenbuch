package de.sg.computerinsel.tools.rest;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.rest.model.UserDTO;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import de.sg.computerinsel.tools.service.SecurityService;

@RestController
public class BaseRestController {

    private static final int DECEMBER_26 = 26;

    private static final int JANUARY_5 = 5;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private MitarbeiterService mitarbeiterService;

    @PostMapping("/loginsecure")
    public Map<String, String> loginsecure(@RequestBody final UserDTO model) {
        final Map<String, String> result = new HashMap<>();
        try {
            securityService.autologin(model.getUsername(), model.getPassword());
            result.put(Message.SUCCESS.getCode(),
                    messageService.get("login.success", SecurityContextHolder.getContext().getAuthentication().getName()));
        } catch (final Exception e) {
            result.put(Message.ERROR.getCode(), messageService.get("login.error"));
        }
        return result;
    }

    @GetMapping("/current-user")
    public String getCurrentUser() {
        return mitarbeiterService.getAngemeldeterMitarbeiter().orElse(new Mitarbeiter()).getCompleteName();
    }

    @GetMapping("/current-filiale")
    public String getCurrentFiliale() {
        final Mitarbeiter mitarbeiter = mitarbeiterService.getAngemeldeterMitarbeiter().orElse(new Mitarbeiter());
        return mitarbeiter.getFiliale() == null ? null : mitarbeiter.getFiliale().getKuerzel();
    }

    @GetMapping("show-inventur-hinweis")
    public boolean showInventurHinweis() {
        return isAfter26December() || isBefore5January();
    }

    private boolean isBefore5January() {
        final LocalDate today = LocalDate.now();
        return today.getMonth() == Month.JANUARY && today.getDayOfMonth() < JANUARY_5;
    }

    private boolean isAfter26December() {
        final LocalDate today = LocalDate.now();
        return today.getMonth() == Month.DECEMBER && today.getDayOfMonth() > DECEMBER_26;
    }

}
