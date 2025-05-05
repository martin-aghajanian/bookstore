package com.martin.bookstore.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete"),

    CONTENT_READ("content:read"),
    CONTENT_CREATE("content:create"),
    CONTENT_UPDATE("content:update"),
    CONTENT_DELETE("content:delete"),

    DATA_IMPORT("data:import"),
    DATA_READ("data:read"),
    DATA_UPDATE("data:update"),
    DATA_CREATE("data:create"),
    DATA_DELETE("data:delete"),


    INVENTORY_CREATE("inventory:create"),
    INVENTORY_UPDATE("inventory:update");
    ;

    @Getter
    private final String permission;
}
