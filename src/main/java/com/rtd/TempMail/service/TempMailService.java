package com.rtd.TempMail.service;

import com.rtd.TempMail.model.Email;
import com.rtd.TempMail.model.TempEmail;
import com.rtd.TempMail.repository.EmailRepository;
import com.rtd.TempMail.repository.TempEmailRepository;
import com.rtd.TempMail.config.DomainConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class TempMailService {
    private final TempEmailRepository tempEmailRepository;
    private final EmailRepository emailRepository;
    private final DomainConfig domainConfig;
    
    public TempEmail generateTempEmail() {
        TempEmail tempEmail = new TempEmail();
        String emailAddress;
        do {
            emailAddress = generateRandomEmail();
            // emailAddress = "bharatjangir950@gmail.com";
        } while (tempEmailRepository.existsByEmailAddress(emailAddress));
        
        tempEmail.setEmailAddress(emailAddress);
        return tempEmailRepository.save(tempEmail);
    }
    
    public List<Email> getEmails(String emailAddress) {
        TempEmail tempEmail = tempEmailRepository.findByEmailAddress(emailAddress)
            .orElseThrow(() -> new RuntimeException("Email address not found"));
            
        if (tempEmail.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Email address has expired");
        }
        
        return emailRepository.findByToAndIsDeletedFalseOrderByReceivedAtDesc(emailAddress);
    }
    
    public void receiveEmail(Email email) {
        if (email.getTo() == null || email.getFrom() == null) {
            throw new RuntimeException("From and To addresses are required");
        }
        
        TempEmail tempEmail = tempEmailRepository.findByEmailAddress(email.getTo())
            .orElseThrow(() -> new RuntimeException("Recipient email address not found"));
            
        if (tempEmail.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Email address has expired");
        }
        
        email.setReceivedAt(LocalDateTime.now());
        emailRepository.save(email);
    }
    
    private String generateRandomEmail() {
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder username = new StringBuilder();
        Random random = new Random();
        
        // Add test- prefix for easier filtering
        username.append("test-");
        
        for(int i = 0; i < 8; i++) {
            username.append(chars.charAt(random.nextInt(chars.length())));
        }
        return username.toString() + "@gmail.com";
    }
} 