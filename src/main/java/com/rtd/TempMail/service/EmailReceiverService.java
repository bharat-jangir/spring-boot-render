package com.rtd.TempMail.service;

import com.rtd.TempMail.model.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FlagTerm;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailReceiverService {
    private final Session emailSession;
    private final TempMailService tempMailService;

    @Scheduled(fixedDelay = 30000) // Checks every 30 seconds
    public void checkEmails() {
        Store store = null;
        Folder inbox = null;
        try {
            store = emailSession.getStore("imaps");
            store.connect();
            log.info("Connected to email store successfully");
            
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            log.info("Found {} unread messages", messages.length);

            for (Message message : messages) {
                try {
                    processMessage(message);
                    message.setFlag(Flags.Flag.SEEN, true);
                } catch (Exception e) {
                    log.error("Error processing message: {}", e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error checking emails: {}", e.getMessage(), e);
        } finally {
            try {
                if (inbox != null && inbox.isOpen()) {
                    inbox.close(false);
                }
                if (store != null && store.isConnected()) {
                    store.close();
                }
            } catch (MessagingException e) {
                log.error("Error closing connections: {}", e.getMessage(), e);
            }
        }
    }

    private void processMessage(Message message) throws MessagingException, IOException {
        if (message.getFrom() == null || message.getFrom().length == 0) {
            log.warn("Skipping message with no sender");
            return;
        }

        Email email = new Email();
        email.setFrom(message.getFrom()[0].toString());
        email.setTo(message.getAllRecipients() != null ? message.getAllRecipients()[0].toString() : null);
        email.setSubject(message.getSubject() != null ? message.getSubject() : "(no subject)");
        email.setContent(getTextFromMessage(message));
        email.setReceivedAt(LocalDateTime.now());

        tempMailService.receiveEmail(email);
        log.info("Successfully processed email from: {}", email.getFrom());
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            }
        }
        return result.toString();
    }
} 