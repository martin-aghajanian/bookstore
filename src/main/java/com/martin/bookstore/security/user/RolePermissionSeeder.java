package com.martin.bookstore.security.user;

import com.martin.bookstore.entity.Permission;
import com.martin.bookstore.entity.Role;
import com.martin.bookstore.repository.PermissionRepository;
import com.martin.bookstore.repository.RoleRepository;
import com.martin.bookstore.security.user.enums.PermissionEnum;
import com.martin.bookstore.security.user.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolePermissionSeeder {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public void seed() {
        Map<String, Permission> existingPermissions = permissionRepository.findAll()
                .stream()
                .collect(
                        Collectors.toMap(Permission::getName, p -> p)
                );

        Map<String, Role> existingRoles = roleRepository.findAll()
                .stream()
                .collect(
                        Collectors.toMap(Role::getName, r -> r)
                );

        List<Permission> newPermissions = Arrays.stream(PermissionEnum.values())
                .map(PermissionEnum::getPermission)
                .filter(permissionName -> !existingPermissions.containsKey(permissionName))
                .map(name -> Permission.builder().name(name).build())
                .toList();

        permissionRepository.saveAll(newPermissions);

        for (RoleEnum enumRole : RoleEnum.values()) {
            String roleName = "ROLE_" + enumRole.name();

            Set<Permission> permissions = enumRole.getPermissionEnums().stream()
                    .map(p -> permissionRepository.findByName(p.getPermission())
                            .orElseThrow(() -> new RuntimeException("Permission not found: " + p.getPermission())))
                    .collect(Collectors.toSet());

            Role role = existingRoles.getOrDefault(roleName, Role.builder().name(roleName).build());
            role.setPermissions(permissions);
            roleRepository.save(role);
        }
    }
}


