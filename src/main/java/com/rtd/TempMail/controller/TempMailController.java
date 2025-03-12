package com.rtd.TempMail.controller;

import com.rtd.TempMail.model.Email;
import com.rtd.TempMail.model.TempEmail;
import com.rtd.TempMail.service.TempMailService;
import com.rtd.TempMail.service.TestEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tempmail")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TempMailController {
    private final TempMailService tempMailService;
    private final TestEmailService testEmailService;
    
    @PostMapping("/generate")
    public ResponseEntity<?> generateTempEmail() {
        try {
            TempEmail tempEmail = tempMailService.generateTempEmail();
            // Send test email automatically after generation
            testEmailService.sendTestEmail(tempEmail.getEmailAddress());
            return ResponseEntity.ok(tempEmail);
        } catch (Exception e) {
            log.error("Error generating email: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/emails/{emailAddress}")
    public ResponseEntity<?> getEmails(@PathVariable String emailAddress) {
        try {
            List<Email> emails = tempMailService.getEmails(emailAddress);
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            log.error("Error fetching emails: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/receive")
    public ResponseEntity<?> receiveEmail(@RequestBody Email email) {
        try {
            tempMailService.receiveEmail(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error receiving email: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/test/{emailAddress}")
    public ResponseEntity<?> sendTestEmail(@PathVariable String emailAddress) {
        try {
            testEmailService.sendTestEmail(emailAddress);
            return ResponseEntity.ok()
                .body(Map.of("message", "Test email sent successfully to " + emailAddress));
        } catch (Exception e) {
            log.error("Error sending test email: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
} 