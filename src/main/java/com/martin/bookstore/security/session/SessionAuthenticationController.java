package com.martin.bookstore.security.session;

import com.martin.bookstore.dto.request.AuthenticationRequest;
import com.martin.bookstore.dto.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/session-auth")
@RequiredArgsConstructor
public class SessionAuthenticationController {

    private final SessionAuthenticationService sessionAuthService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        sessionAuthService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<SessionAuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(sessionAuthService.authenticate(request, httpRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Session-Id") String sessionId
    ) {
        sessionAuthService.invalidateSession(sessionId);
        return ResponseEntity.ok().build();
    }
}
