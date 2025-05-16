package com.martin.bookstore.controller;

import com.martin.bookstore.service.JwtAuthenticationService;
import com.martin.bookstore.dto.request.AuthenticationRequest;
import com.martin.bookstore.dto.response.AuthenticationResponse;
import com.martin.bookstore.dto.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/jwt-auth")
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final JwtAuthenticationService jwtAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(jwtAuthenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(jwtAuthenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        jwtAuthenticationService.refreshToken(request, response);
    }

}