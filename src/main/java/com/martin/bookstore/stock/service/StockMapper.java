package com.martin.bookstore.stock.service;

import com.martin.bookstore.stock.dto.StockResponseDto;
import com.martin.bookstore.stock.persistence.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {
    public StockResponseDto asOutput(Stock stock) {
        StockResponseDto dto = new StockResponseDto();
        dto.setBookId(stock.getBook().getId());
        dto.setIsbn(stock.getBook().getIsbn());
        dto.setQuantityAvailable(stock.getQuantityAvailable());
        dto.setQuantityReserved(stock.getQuantityReserved());
        dto.setQuantitySold(stock.getQuantitySold());
        return dto;
    }
}
