package com.martin.bookstore.shared.exception;

public class WishlistNotFoundException extends RuntimeException {
    public WishlistNotFoundException(Long userId) {
        super("Wishlist not found for user with ID: " + userId);
    }

    public WishlistNotFoundException() {
        super("Wishlist not found for the current user.");
    }
}
