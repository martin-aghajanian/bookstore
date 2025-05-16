package com.martin.bookstore.security.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findBySessionIdAndIsActiveTrue(String sessionId);
}
