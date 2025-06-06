package com.martin.bookstore.cart.service;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.cart.dto.CartFullResponseDto;
import com.martin.bookstore.cart.dto.CartResponseDto;
import com.martin.bookstore.cart.persistence.entity.Cart;
import com.martin.bookstore.cart.persistence.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponseDto toCartResponseDto(CartItem item) {
        Book book = item.getBook();

        return new CartResponseDto(
                item.getId(),
                book.getId(),
                book.getTitle(),
                item.getQuantity(),
                book.getPrice()
        );
    }

    public CartFullResponseDto toCartFullResponseDto(Cart cart) {
        List<CartResponseDto> items = cart.getItems()
                .stream()
                .map(this::toCartResponseDto)
                .collect(Collectors.toList());

        int totalItems = items.stream().mapToInt(CartResponseDto::getQuantity).sum();
        double totalPrice = items.stream()
                .mapToDouble(i -> i.getQuantity() * i.getBookPrice())
                .sum();

        return new CartFullResponseDto(
                cart.getId(),
                cart.getUser().getId(),
                items,
                totalItems,
                totalPrice
        );
    }
}
