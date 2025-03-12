package com.rtd.TempMail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {
    @Value("${mail.domain:tempmail.com}")
    private String emailDomain;

    public String getEmailDomain() {
        return emailDomain;
    }
} 