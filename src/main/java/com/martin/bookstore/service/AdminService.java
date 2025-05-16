package com.martin.bookstore.service;

import com.martin.bookstore.entity.Permission;
import com.martin.bookstore.entity.Role;
import com.martin.bookstore.entity.User;
import com.martin.bookstore.exception.NotFoundException;
import com.martin.bookstore.repository.PermissionRepository;
import com.martin.bookstore.repository.RoleRepository;
import com.martin.bookstore.repository.UserRepository;
import com.martin.bookstore.security.admin.ModifyUserAccessRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public void modifyUserAccess(ModifyUserAccessRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        switch (request.getType().toUpperCase()) {
            case "ROLE" -> {
                if (request.getAction().equalsIgnoreCase("ASSIGN")) {
                    Role role = roleRepository.findByName(request.getValue())
                            .orElseThrow(() -> new NotFoundException("Role not found"));
                    user.setRole(role);
                } else if (request.getAction().equalsIgnoreCase("REMOVE")) {
                    Role defaultRole = roleRepository.findByName("ROLE_USER")
                            .orElseThrow(() -> new NotFoundException("Default role ROLE_USER not found"));
                    user.setRole(defaultRole);
                }
            }
            case "PERMISSION" -> {
                Permission permission = permissionRepository.findByName(request.getValue())
                        .orElseThrow(() -> new NotFoundException("Permission not found"));
                if (request.getAction().equalsIgnoreCase("ASSIGN")) {
                    user.getPermissions().add(permission);
                } else if (request.getAction().equalsIgnoreCase("REMOVE")) {
                    user.getPermissions().remove(permission);
                }
            }
            default -> throw new IllegalArgumentException("Invalid type: must be ROLE or PERMISSION");
        }

        userRepository.save(user);
    }
}
