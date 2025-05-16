package com.martin.bookstore.security.session;

import com.martin.bookstore.security.dto.AuthenticationRequest;
import com.martin.bookstore.security.dto.RegisterRequest;
import com.martin.bookstore.security.exception.DefaultRoleNotFoundException;
import com.martin.bookstore.security.exception.EmailAlreadyTakenException;
import com.martin.bookstore.security.exception.UsernameAlreadyTakenException;
import com.martin.bookstore.security.user.RoleRepository;
import com.martin.bookstore.security.user.User;
import com.martin.bookstore.security.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SessionAuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserSessionRepository userSessionRepository;
    private final SessionAuthProperties sessionAuthProperties;

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTakenException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyTakenException("Email '" + request.getEmail() + "' is already registered");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new DefaultRoleNotFoundException("Default role USER not found")))
                .build();

        userRepository.save(user);
    }

    public SessionAuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest httpRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String sessionId = UUID.randomUUID().toString();

        UserSession session = new UserSession();
        session.setSessionId(sessionId);
        session.setUserId(user.getId());
        session.setIpAddress(httpRequest.getRemoteAddr());
        session.setUserAgent(httpRequest.getHeader("User-Agent"));
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(sessionAuthProperties.getExpirationMinutes()));
        session.setActive(true);

        userSessionRepository.save(session);

        return SessionAuthenticationResponse.builder()
                .sessionId(sessionId)
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }

    public void invalidateSession(String sessionId) {
        userSessionRepository.findBySessionIdAndIsActiveTrue(sessionId)
                .ifPresent(session -> {
                    session.setActive(false);
                    userSessionRepository.save(session);
                });
    }
}
