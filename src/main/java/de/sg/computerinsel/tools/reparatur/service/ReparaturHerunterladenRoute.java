package de.sg.computerinsel.tools.reparatur.service;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import de.sg.computerinsel.tools.service.EinstellungenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReparaturHerunterladenRoute extends RouteBuilder {

    private final EinstellungenService einstellungService;

    @Override
    public void configure() throws Exception {
        final String cron = RegExUtils.replaceAll(einstellungService.getRoboterCron().getWert(), StringUtils.SPACE, "+");
        if (StringUtils.isNotBlank(cron) && StringUtils.isNotBlank(einstellungService.getFtpHost().getWert())) {
            try {
                CronExpression.parse(cron);
            } catch (final IllegalArgumentException e) {
                log.error(e.getMessage(), e);
                return;
            }
            final String hostname = einstellungService.getFtpHost().getWert();
            final String port = einstellungService.getFtpPort().getWert();
            final String username = einstellungService.getFtpUser().getWert();
            final String password = einstellungService.getFtpPassword().getWert();

            final File file = new File(einstellungService.getAblageverzeichnis().getWert(), "reparaturen");

            from("ftp://" + username + "@" + hostname + ":" + port + "/reparaturen/?scheduler=quartz&scheduler.cron=" + cron + "&password="
                    + password + "&passiveMode=true&delete=true&include=.*\\.csv$").to("file://" + file.getAbsolutePath());
        }

    }

}
