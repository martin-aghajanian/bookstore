package com.martin.bookstore.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponseDto {
    private Long id;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private String username;
}
