package com.martin.bookstore.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.martin.bookstore.security.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(
            EnumSet.noneOf(Permission.class)
    ),

    ADMIN(
            EnumSet.allOf(Permission.class)
    ),

    MANAGER(
            EnumSet.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    ),

    CONTENT_EDITOR(
            EnumSet.of(
                    CONTENT_READ,
                    CONTENT_CREATE,
                    CONTENT_UPDATE,
                    CONTENT_DELETE
            )
    ),

    DATA_IMPORTER(
            EnumSet.of(
                    DATA_IMPORT,
                    DATA_READ,
                    DATA_UPDATE,
                    DATA_CREATE,
                    DATA_DELETE
            )
    ),

    INVENTORY_MANAGER(
            EnumSet.of(
                    INVENTORY_CREATE,
                    INVENTORY_UPDATE
            )
    ),

    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
