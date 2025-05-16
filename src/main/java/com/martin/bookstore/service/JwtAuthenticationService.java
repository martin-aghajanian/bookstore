package com.martin.bookstore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.bookstore.security.config.JwtAuthProperties;
import com.martin.bookstore.dto.request.AuthenticationRequest;
import com.martin.bookstore.dto.response.AuthenticationResponse;
import com.martin.bookstore.dto.request.RegisterRequest;
import com.martin.bookstore.security.config.JwtService;
import com.martin.bookstore.exception.DefaultRoleNotFoundException;
import com.martin.bookstore.exception.EmailAlreadyTakenException;
import com.martin.bookstore.exception.UsernameAlreadyTakenException;
import com.martin.bookstore.security.config.CustomUserDetails;
import com.martin.bookstore.repository.RoleRepository;
import com.martin.bookstore.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.martin.bookstore.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthProperties jwtAuthProperties;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTakenException(
                    "Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyTakenException(
                    "Email '" + request.getEmail() + "' is already registered");
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


        CustomUserDetails userDetails = new CustomUserDetails(user);

        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String authType = jwtAuthProperties.getAuthType();
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith(authType)) {
            return;
        }
        refreshToken = authHeader.substring(authType.length()).trim();
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            CustomUserDetails userDetails = new CustomUserDetails(user);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtService.generateToken(userDetails);

                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
