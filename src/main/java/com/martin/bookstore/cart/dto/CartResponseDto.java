package com.martin.bookstore.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartResponseDto {

    private Long cartItemId;
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
    private Double bookPrice;
}
