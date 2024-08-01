package com.example.securetalk.service;

import com.example.securetalk.model.Session;
import com.example.securetalk.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public void saveSession(Long userId, String sessionId, LocalDateTime expiresAt) {
        Session session = new Session();
        session.setUserId(userId);
        session.setSessionId(sessionId);
        session.setExpiresAt(expiresAt);
        sessionRepository.save(session);
    }

    public Session getSessionBySessionId(String sessionId) {
        return sessionRepository.findBySessionId(sessionId);
    }
}
