package com.martin.bookstore.controller;

import com.martin.bookstore.security.admin.ModifyUserAccessRequestDto;
import com.martin.bookstore.security.admin.RolePermissionSeeder;
import com.martin.bookstore.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
public class AdminController {
    private final RolePermissionSeeder rolePermissionSeeder;
    private final AdminService adminService;

    @PostMapping("/seed-roles-permissions")
    @PreAuthorize("hasAuthority('admin:update')")
    public void seedRolesAndPermissions() {
        rolePermissionSeeder.seed();
    }

    @PostMapping("/modify-user-access")
    @PreAuthorize("hasAnyAuthority('admin:update')")
    public void modifyUserAccess(@RequestBody ModifyUserAccessRequestDto request) {
        adminService.modifyUserAccess(request);
    }
}
