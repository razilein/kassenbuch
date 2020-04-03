package de.sg.computerinsel.tools.kunde.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.sg.computerinsel.tools.angebot.dto.AngebotDto;
import de.sg.computerinsel.tools.angebot.service.AngebotService;
import de.sg.computerinsel.tools.kunde.model.Anrede;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.kunde.service.EmailService;
import de.sg.computerinsel.tools.kunde.service.KundeService;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.rechnung.service.RechnungService;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.reparatur.service.ReparaturService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.service.MessageService;

@RestController
@RequestMapping("/email")
public class KundeEmailRestController {

    @Autowired
    private AngebotService angebotService;

    @Autowired
    private EmailService service;

    @Autowired
    private MessageService messageService;

    @Autowired
    private KundeService kundeService;

    @Autowired
    private RechnungService rechnungService;

    @Autowired
    private ReparaturService reparaturService;

    @PostMapping("/rechnung")
    public Map<String, Object> sendeMailRechnung(@RequestParam("file") final MultipartFile file, @RequestParam("id") final Integer id,
            @RequestParam("anrede") final String anrede) {
        final Map<String, Object> result = new HashMap<>();

        final Rechnung rechnung = rechnungService.getRechnung(id).getRechnung();
        service.sendeEmail(file, rechnung, anrede);
        result.put(Message.SUCCESS.getCode(), messageService.get("email.success"));
        updateKundeAnrede(rechnung.getKunde(), anrede);
        return result;
    }

    @PostMapping("/angebot")
    public Map<String, Object> sendeMailAngebot(@RequestParam("files") final List<MultipartFile> files,
            @RequestParam("id") final Integer id, @RequestParam("anrede") final String anrede) {
        final Map<String, Object> result = new HashMap<>();

        final AngebotDto dto = angebotService.getAngebot(id);
        service.sendeEmail(files, dto, anrede);
        result.put(Message.SUCCESS.getCode(), messageService.get("email.success"));
        updateKundeAnrede(dto.getAngebot().getKunde(), anrede);
        return result;
    }

    @PostMapping("/reparatur")
    public Map<String, Object> sendeMailReparaturauftrag(@RequestBody final Map<String, Object> params) {
        final Map<String, Object> result = new HashMap<>();

        final Integer id = (Integer) params.get("id");
        final Optional<Reparatur> optional = reparaturService.getReparatur(id);
        if (optional.isPresent()) {
            final String anrede = (String) params.get("anrede");
            service.sendeEmail(optional.get(), anrede);
            result.put(Message.SUCCESS.getCode(), messageService.get("email.success"));
            updateKundeAnrede(optional.get().getKunde(), anrede);
        } else {
            result.put(Message.ERROR.getCode(), messageService.get("email.reparatur.error", id));
        }
        return result;
    }

    private void updateKundeAnrede(final Kunde kunde, final String anrede) {
        if (kunde.getAnrede() == null) {
            kunde.setAnrede(Anrede.getByBriefAnrede(anrede).getCode());
            if (kunde.getAnrede() != null) {
                kundeService.save(kunde);
            }
        }
    }

}
