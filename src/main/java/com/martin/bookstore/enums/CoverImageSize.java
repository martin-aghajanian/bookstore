package com.martin.bookstore.enums;

import lombok.Getter;

public enum CoverImageSize {
    SMALL(100, 150),
    MEDIUM(200, 300),
    LARGE(400, 600);

    @Getter
    private final int width;
    @Getter
    private final int height;

    CoverImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}