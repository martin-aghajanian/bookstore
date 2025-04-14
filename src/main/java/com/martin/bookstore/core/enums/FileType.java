package com.martin.bookstore.core.enums;

import lombok.Getter;

public enum FileType {
    JPG("jpg"),
    UNKNOWN("unknown");

    @Getter
    private final String type;

    FileType(String type) {
        this.type = type;
    }
}
