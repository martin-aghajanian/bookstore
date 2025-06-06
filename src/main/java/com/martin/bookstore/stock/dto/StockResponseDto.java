package com.martin.bookstore.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponseDto {
    private Long bookId;
    private Long isbn;
    private int quantityAvailable;
    private int quantityReserved;
    private int quantitySold;
}