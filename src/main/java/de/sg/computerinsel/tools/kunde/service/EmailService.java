package de.sg.computerinsel.tools.kunde.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.sg.computerinsel.tools.angebot.dto.AngebotDto;
import de.sg.computerinsel.tools.angebot.model.Angebot;
import de.sg.computerinsel.tools.kunde.model.Kunde;
import de.sg.computerinsel.tools.rechnung.model.Rechnung;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;
import de.sg.computerinsel.tools.service.EinstellungenService;
import de.sg.computerinsel.tools.service.MessageService;
import de.sg.computerinsel.tools.service.MitarbeiterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class EmailService {

    private static final String PLACEHOLDER_NUMMER = "#NUMMER#";

    private final EinstellungenService einstellungService;

    private final MessageService messageService;

    private final MitarbeiterService mitarbeiterService;

    public void sendeEmail(final MultipartFile file, final Rechnung rechnung, final String anrede) {
        final String nummer = rechnung.getFiliale().getKuerzel() + rechnung.getNummerAnzeige();
        File rechnungFile = null;
        try {
            final Kunde kunde = rechnung.getKunde();

            final Session session = initSmtpSession();
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(rechnung.getFiliale().getEmail()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(kunde.getEmail()));
            message.setSubject("Rechnung " + nummer);

            final StringBuilder builder = new StringBuilder();
            setMailHeader(kunde, anrede, builder);
            builder.append(RegExUtils.replaceAll(einstellungService.getMailBodyRechnung().getWert(), PLACEHOLDER_NUMMER, nummer));
            setMailFooter(builder);

            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(builder.toString(), StandardCharsets.UTF_8.name());

            final MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            rechnungFile = File.createTempFile("Rechnung_" + rechnung.getNummer() + "_", ".pdf");
            FileUtils.writeByteArrayToFile(rechnungFile, file.getBytes());
            attachmentBodyPart.attachFile(rechnungFile);

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            addToSentFolder(message, session);
        } catch (final MessagingException | IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            FileUtils.deleteQuietly(rechnungFile);
        }

    }

    public void sendeEmail(final Reparatur reparatur, final String anrede) {
        final String nummer = reparatur.getFiliale().getKuerzel() + reparatur.getNummer();
        try {
            final Kunde kunde = reparatur.getKunde();

            final Session session = initSmtpSession();
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(reparatur.getFiliale().getEmail()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(kunde.getEmail()));
            message.setSubject("Reparaturauftrag " + nummer);

            final StringBuilder builder = new StringBuilder();
            setMailHeader(kunde, anrede, builder);
            builder.append(RegExUtils.replaceAll(einstellungService.getMailBodyReparaturauftrag().getWert(), PLACEHOLDER_NUMMER, nummer));
            setMailFooter(builder);

            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(builder.toString(), StandardCharsets.UTF_8.name());

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            addToSentFolder(message, session);
        } catch (final MessagingException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void sendeEmail(final MultipartFile file, final AngebotDto dto, final String anrede) {
        final Angebot angebot = dto.getAngebot();
        final String nummer = angebot.getFiliale().getKuerzel() + angebot.getNummer();
        File angebotFile = null;
        try {
            final Kunde kunde = angebot.getKunde();

            final Session session = initSmtpSession();
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(angebot.getFiliale().getEmail()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(kunde.getEmail()));
            message.setSubject("Angebot " + nummer);

            final StringBuilder builder = new StringBuilder();
            setMailHeader(kunde, anrede, builder);
            builder.append(RegExUtils.replaceAll(einstellungService.getMailBodyAngebot().getWert(), PLACEHOLDER_NUMMER, nummer));
            setMailFooter(builder);

            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(builder.toString(), StandardCharsets.UTF_8.name());

            final MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            angebotFile = File.createTempFile("Angebot_" + angebot.getNummer() + "_", ".pdf");
            FileUtils.writeByteArrayToFile(angebotFile, file.getBytes());
            attachmentBodyPart.attachFile(angebotFile);

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            addToSentFolder(message, session);
        } catch (final MessagingException | IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            FileUtils.deleteQuietly(angebotFile);
        }

    }

    private void addToSentFolder(final Message message, final Session session) {
        try (Store store = session.getStore("imap");) {
            store.connect(einstellungService.getSmtpHost().getWert(), einstellungService.getSmtpUser().getWert(),
                    einstellungService.getSmtpPassword().getWert());
            final Folder folder = store.getFolder("Gesendet");
            folder.open(Folder.READ_WRITE);
            message.setFlag(Flag.SEEN, true);
            folder.appendMessages(new Message[] { message });
        } catch (final MessagingException e) {
            log.error("Fehler beim Ablegen der E-Mail im Gesendet-Order", e.getMessage(), e);
        }
    }

    private void setMailFooter(final StringBuilder builder) {
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());
        builder.append(messageService.get("email.signature"));
        builder.append(System.lineSeparator());
        builder.append(mitarbeiterService.getAngemeldeterMitarbeiterVornameNachname());
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());
        builder.append(einstellungService.getMailSignatur().getWert());
    }

    private void setMailHeader(final Kunde kunde, final String anrede, final StringBuilder builder) {
        builder.append(StringUtils.isNotBlank(anrede) ? anrede : kunde.getBriefanrede());
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());
    }

    private Session initSmtpSession() {
        final Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.host", einstellungService.getSmtpHost().getWert());
        prop.put("mail.smtp.port", einstellungService.getSmtpPort().getWert());
        prop.put("mail.store.protocol", "imaps");
        prop.put("mail.mime.charset", StandardCharsets.UTF_8.name());

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(einstellungService.getSmtpUser().getWert(),
                        einstellungService.getSmtpPassword().getWert());
            }
        });
    }

}
