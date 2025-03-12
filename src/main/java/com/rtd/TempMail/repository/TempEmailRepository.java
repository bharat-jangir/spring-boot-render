package com.rtd.TempMail.repository;

import com.rtd.TempMail.model.TempEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TempEmailRepository extends JpaRepository<TempEmail, Long> {
    Optional<TempEmail> findByEmailAddress(String emailAddress);
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
    boolean existsByEmailAddress(String emailAddress);
} 