package de.sg.computerinsel.tools.inventar.rest;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sg.computerinsel.tools.inventar.service.InventurService;
import de.sg.computerinsel.tools.rest.Message;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.ProtokollService;

@RestController
@RequestMapping("/inventar/inventur")
public class InventurRestController {

    @Autowired
    private InventurService service;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProtokollService protokollService;

    @PostMapping
    public Map<String, Object> startInventur() {
        service.starteInventur();
        protokollService.write(messageService.get("protokoll.inventur"));
        return Collections.singletonMap(Message.SUCCESS.getCode(), messageService.get("inventar.inventur.success"));
    }

}
