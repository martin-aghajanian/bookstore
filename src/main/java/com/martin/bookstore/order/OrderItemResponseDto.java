package com.martin.bookstore.order;

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
