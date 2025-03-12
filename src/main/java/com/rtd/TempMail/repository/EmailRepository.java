package com.rtd.TempMail.repository;

import com.rtd.TempMail.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findByToAndIsDeletedFalseOrderByReceivedAtDesc(String to);
    List<Email> findByToAndIsDeletedTrue(String to);
    void deleteAllByTo(String to);
} 