package com.martin.bookstore.stock.controller;

import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.stock.dto.LowStockBookResponseDto;
import com.martin.bookstore.stock.dto.StockResponseDto;
import com.martin.bookstore.stock.service.StockService;
import com.martin.bookstore.stock.dto.StockUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/{bookId}/restock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restock(@PathVariable Long bookId, @RequestParam int amount) {
        stockService.restock(bookId, amount);
    }

    @PostMapping("/initialize")
    @ResponseStatus(HttpStatus.CREATED)
    public void initializeAllMissingStocks() {
        stockService.initializeMissingStocks();
    }

    @GetMapping("/{bookId}")
    public StockResponseDto getStock(@PathVariable Long bookId) {
        return stockService.getStockInfo(bookId);
    }

    @GetMapping("/low")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<LowStockBookResponseDto> getLowStockBooks(
            @RequestParam(defaultValue = "10") int threshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return stockService.getLowStockBooks(threshold, PageRequest.of(page, size));
    }

    @PutMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public StockResponseDto updateStock(
            @PathVariable Long bookId,
            @RequestBody @Valid StockUpdateRequestDto dto
    ) {
        return stockService.updateStock(bookId, dto);
    }
}

