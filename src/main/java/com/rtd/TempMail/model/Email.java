package com.rtd.TempMail.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import java.time.LocalDateTime;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @jakarta.validation.constraints.Email
    @Column(name = "from_address")
    private String from;
    
    @NotBlank
    @jakarta.validation.constraints.Email
    @Column(name = "to_address")
    private String to;
    
    @NotBlank
    private String subject;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private LocalDateTime receivedAt;
    
    @Column(nullable = false)
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        receivedAt = LocalDateTime.now();
    }
} 