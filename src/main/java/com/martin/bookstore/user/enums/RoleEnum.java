package com.martin.bookstore.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.martin.bookstore.user.enums.PermissionEnum.*;

@RequiredArgsConstructor
public enum RoleEnum {
    USER(
            EnumSet.of(
                    USER_READ,
                    USER_UPDATE,
                    USER_CREATE,
                    USER_DELETE
            )
    ),

    ADMIN(
            EnumSet.allOf(PermissionEnum.class)
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
    private final Set<PermissionEnum> permissionEnums;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissionEnums()
                .stream()
                .map(permissionEnum -> new SimpleGrantedAuthority(permissionEnum.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
