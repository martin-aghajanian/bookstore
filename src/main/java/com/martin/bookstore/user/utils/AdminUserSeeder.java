package com.martin.bookstore.user.utils;

import com.martin.bookstore.user.persistence.entity.Role;
import com.martin.bookstore.user.persistence.entity.User;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.user.persistence.repository.RoleRepository;
import com.martin.bookstore.user.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public void createAdminIfNotExists() {
        String username = "admin_martin";

        if (userRepository.existsByUsername(username)) {
            return;
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new NotFoundException("ROLE_ADMIN not found. Seed roles first."));

        User admin = User.builder()
                .username(username)
                .email("martin.aghajanian@gmail.com")
                .firstName("Martin")
                .lastName("Aghajanian")
                .password(passwordEncoder.encode("Admin123!"))
                .role(adminRole)
                .build();

        userRepository.save(admin);
    }
}

