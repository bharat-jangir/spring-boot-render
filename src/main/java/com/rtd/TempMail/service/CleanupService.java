package com.rtd.TempMail.service;

import com.rtd.TempMail.repository.TempEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CleanupService {
    private final TempEmailRepository tempEmailRepository;

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void cleanupExpiredEmails() {
        tempEmailRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
} 