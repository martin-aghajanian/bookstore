package com.martin.bookstore.security.session;


import com.martin.bookstore.security.config.CustomUserDetails;
import com.martin.bookstore.entity.User;
import com.martin.bookstore.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;
    private final SessionAuthProperties sessionAuthProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String sessionId = request.getHeader(sessionAuthProperties.getHeaderName());

        if (StringUtils.hasText(sessionId)) {
            userSessionRepository.findBySessionIdAndIsActiveTrue(sessionId).ifPresent(session -> {
                if (session.getExpiresAt().isAfter(java.time.LocalDateTime.now())) {
                    User user = userRepository.findByIdWithRole(session.getUserId()).orElse(null);
                    if (user != null) {
                        CustomUserDetails userDetails = new CustomUserDetails(user);
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            });
        }

        filterChain.doFilter(request, response);
    }
}
