package com.martin.bookstore.core.enums;

import lombok.Getter;

public enum CoverImageFileName {
    FULL("full.jpg"),
    SMALL("small.jpg"),
    MEDIUM("medium.jpg"),
    LARGE("large.jpg");

    @Getter
    private final String fileName;

    CoverImageFileName(String fileName) {
        this.fileName = fileName;
    }
}

