package com.martin.bookstore.wishlist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping("/books/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistResponseDto addBookToWishlist(@PathVariable Long bookId) {
        return wishlistService.addBookToWishlist(bookId);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WishlistResponseDto getUserWishlist() {
        return wishlistService.getAuthenticatedUserWishlist();
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/books/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookFromWishlist(@PathVariable Long bookId) {
        wishlistService.removeBookFromWishlist(bookId);
    }
}
