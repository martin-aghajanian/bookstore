package com.martin.bookstore.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class LowStockBookResponseDto {
    private Long bookId;
    private String title;
    private Long isbn;
    private int quantityAvailable;
}
