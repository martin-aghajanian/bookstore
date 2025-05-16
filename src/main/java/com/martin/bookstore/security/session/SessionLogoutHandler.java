package com.martin.bookstore.security.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class SessionLogoutHandler implements LogoutHandler {

    private final UserSessionRepository userSessionRepository;
    private final SessionAuthProperties sessionAuthProperties;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String sessionId = request.getHeader(sessionAuthProperties.getHeaderName());
        if (!StringUtils.hasText(sessionId)) {
            return;
        }

        userSessionRepository.findBySessionIdAndIsActiveTrue(sessionId).ifPresent(session -> {
            session.setActive(false);
            userSessionRepository.save(session);
        });
    }
}
