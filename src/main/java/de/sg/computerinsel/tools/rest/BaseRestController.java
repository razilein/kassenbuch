package de.sg.computerinsel.tools.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.rest.model.UserDTO;
import de.sg.computerinsel.tools.service.SecurityService;

@RestController
public class BaseRestController {

    @Autowired
    private SecurityService securityService;

    @RequestMapping("/login")
    public String login(final Model model, final String error, final String logout) {
        return "login";
    }

    @PostMapping("/loginsecure")
    public Map<String, String> login(@RequestBody final UserDTO model) {
        final Map<String, String> result = new HashMap<>();
        try {
            securityService.autologin(model.getUsername(), model.getPassword());
            result.put(Message.SUCCESS.getCode(),
                    "Erfolgreich eingeloggt! Willkommen " + SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (final Exception e) {
            result.put(Message.ERROR.getCode(), "Der Login war nicht erfolgreich. Bitte prüfen Sie Ihre Anmeldedaten.");
        }
        return result;
    }

}
