package com.martin.bookstore.stock;

import com.martin.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final BookRepository bookRepository;

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
}

