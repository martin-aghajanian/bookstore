package com.martin.bookstore.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LowStockBookResponseDto {
    private Long bookId;
    private String title;
    private Long isbn;
    private int quantityAvailable;
}
