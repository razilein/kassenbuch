package de.sg.computerinsel.tools.kunde.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/rechnung/{id}")
    public Map<String, Object> getTextRechnung(@PathVariable final Integer id) {
        final Rechnung rechnung = rechnungService.getRechnung(id).getRechnung();
        final String anrede = rechnung.getKunde().getBriefanrede();
        return Collections.singletonMap(Message.SUCCESS.getCode(), service.getMailText(rechnung, anrede));
    }

    @PostMapping("/rechnung")
    public Map<String, Object> sendeMailRechnung(@RequestParam("file") final MultipartFile file, @RequestParam("id") final Integer id,
            @RequestParam("text") final String text) {
        final Map<String, Object> result = new HashMap<>();

        final Rechnung rechnung = rechnungService.getRechnung(id).getRechnung();
        service.sendeEmail(file, rechnung, text);
        result.put(Message.SUCCESS.getCode(), messageService.get("email.success"));
        getAnrede(text).ifPresent(anrede -> updateKundeAnrede(rechnung.getKunde(), anrede));
        return result;
    }

    private Optional<String> getAnrede(final String text) {
        return StringUtils.isNotBlank(text) ? Optional.of(StringUtils.split(text, System.lineSeparator())[0]) : Optional.empty();
    }

    @GetMapping("/angebot/{id}")
    public Map<String, Object> getTextAngebot(@PathVariable final Integer id) {
        final AngebotDto dto = angebotService.getAngebot(id);
        final String anrede = dto.getAngebot().getKunde().getBriefanrede();
        return Collections.singletonMap(Message.SUCCESS.getCode(), service.getMailText(dto.getAngebot(), anrede));
    }

    @PostMapping("/angebot")
    public Map<String, Object> sendeMailAngebot(@RequestParam("files") final List<MultipartFile> files,
            @RequestParam("id") final Integer id, @RequestParam("text") final String text) {
        final Map<String, Object> result = new HashMap<>();

        final AngebotDto dto = angebotService.getAngebot(id);
        service.sendeEmail(files, dto, text);
        result.put(Message.SUCCESS.getCode(), messageService.get("email.success"));
        getAnrede(text).ifPresent(anrede -> updateKundeAnrede(dto.getAngebot().getKunde(), anrede));
        return result;
    }

    @GetMapping("/reparatur/{id}")
    public Map<String, Object> getTextReparatur(@PathVariable final Integer id) {
        final Optional<Reparatur> optional = reparaturService.getReparatur(id);
        if (optional.isPresent()) {
            final Reparatur reparatur = optional.get();
            final String anrede = reparatur.getKunde().getBriefanrede();
            return Collections.singletonMap(Message.SUCCESS.getCode(), service.getMailText(reparatur, anrede));
        }
        return Collections.singletonMap(Message.SUCCESS.getCode(), StringUtils.EMPTY);
    }

    @PostMapping("/reparatur")
    public Map<String, Object> sendeMailReparaturauftrag(@RequestBody final Map<String, Object> params) {
        final Map<String, Object> result = new HashMap<>();

        final Integer id = (Integer) params.get("id");
        final Optional<Reparatur> optional = reparaturService.getReparatur(id);
        if (optional.isPresent()) {
            final String text = (String) params.get("text");
            service.sendeEmail(optional.get(), text);
            result.put(Message.SUCCESS.getCode(), messageService.get("email.success"));
            getAnrede(text).ifPresent(anrede -> updateKundeAnrede(optional.get().getKunde(), anrede));
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
