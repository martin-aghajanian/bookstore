package com.martin.bookstore.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private int rating;
    private String comment;
}
