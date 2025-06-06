package com.martin.bookstore.cart.controller;

import com.martin.bookstore.cart.dto.CartFullResponseDto;
import com.martin.bookstore.cart.dto.CartRequestDto;
import com.martin.bookstore.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasAuthority('user:update')")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public CartFullResponseDto addToCart(@Valid @RequestBody CartRequestDto requestDto) {
        return cartService.addToCart(requestDto);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CartFullResponseDto getUserCart() {
        return cartService.getUserCart();
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/books/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookFromCart(@PathVariable Long bookId) {
        cartService.removeFromCart(bookId);
    }
}
