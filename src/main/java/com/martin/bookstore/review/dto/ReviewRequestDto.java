package com.martin.bookstore.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ReviewRequestDto {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
