package com.martin.bookstore.wishlist.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistRequestDto {
    @NotNull(message = "Book ID is required")
    private Long bookId;
}