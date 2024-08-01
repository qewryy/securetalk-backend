package com.example.securetalk.repository;

import com.example.securetalk.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findBySessionId(String sessionId);
}
