package com.martin.bookstore.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartFullResponseDto {

    private Long cartId;
    private Long userId;
    private List<CartResponseDto> items;
    private int totalItems;
    private double totalPrice;
}
