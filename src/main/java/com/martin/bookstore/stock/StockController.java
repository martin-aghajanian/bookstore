package com.martin.bookstore.stock;

import com.martin.bookstore.dto.response.PageResponseDto;
import com.martin.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/low")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<LowStockBookResponseDto> getLowStockBooks(
            @RequestParam(defaultValue = "10") int threshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return stockService.getLowStockBooks(threshold, PageRequest.of(page, size));
    }
}

