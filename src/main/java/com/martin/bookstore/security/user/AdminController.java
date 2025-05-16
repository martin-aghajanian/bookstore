package com.martin.bookstore.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
public class AdminController {
    private final RolePermissionSeeder rolePermissionSeeder;

    @PostMapping("/seed-roles-permissions")
    @PreAuthorize("hasAuthority('admin:update')")
    public void seedRolesAndPermissions() {
        rolePermissionSeeder.seed();
    }
}
