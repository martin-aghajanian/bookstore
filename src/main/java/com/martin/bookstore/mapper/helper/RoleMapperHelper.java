package com.martin.bookstore.mapper.helper;

import com.martin.bookstore.entity.Role;
import com.martin.bookstore.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapperHelper {

    private final RoleRepository roleRepository;

    public Role map(String name) {
        return roleRepository.findByName(name.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + name));
    }
}
