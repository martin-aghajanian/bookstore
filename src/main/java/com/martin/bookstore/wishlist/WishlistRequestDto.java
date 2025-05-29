package com.martin.bookstore.wishlist;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistRequestDto {
    @NotNull(message = "Book ID is required")
    private Long bookId;
}