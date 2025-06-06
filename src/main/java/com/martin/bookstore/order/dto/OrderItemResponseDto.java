package com.martin.bookstore.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDto {

    private Long bookId;
    private String bookTitle;
    private Integer quantity;
    private Double priceAtPurchase;
}
