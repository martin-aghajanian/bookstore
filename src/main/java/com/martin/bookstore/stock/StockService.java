package com.martin.bookstore.stock;

import com.martin.bookstore.dto.response.PageResponseDto;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.exception.NotFoundException;
import com.martin.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final BookRepository bookRepository;
    private final StockMapper stockMapper;

    @Transactional
    public void restock(Long bookId, int amount) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));

        Stock stock = stockRepository.findByBookId(book.getId());
        if (stock == null) {
            stock = new Stock();
            stock.setBook(book);
            stock.setQuantityAvailable(0);
            stock.setQuantityReserved(0);
            stock.setQuantitySold(0);
        }

        stock.setQuantityAvailable(stock.getQuantityAvailable() + amount);
        stockRepository.save(stock);
    }


    @Transactional
    public boolean reserveStock(Book book, int quantity) {
        Stock stock = stockRepository.findByBookId(book.getId());
        if (stock.getQuantityAvailable() < quantity) return false;

        stock.setQuantityAvailable(stock.getQuantityAvailable() - quantity);
        stock.setQuantityReserved(stock.getQuantityReserved() + quantity);
        stockRepository.save(stock);
        return true;
    }

    @Transactional
    public void finalizePurchase(Book book, int quantity) {
        Stock stock = stockRepository.findByBookId(book.getId());
        stock.setQuantityReserved(stock.getQuantityReserved() - quantity);
        stock.setQuantitySold(stock.getQuantitySold() + quantity);
        stockRepository.save(stock);
    }

    @Transactional
    public void releaseReserved(Book book, int quantity) {
        Stock stock = stockRepository.findByBookId(book.getId());
        stock.setQuantityReserved(stock.getQuantityReserved() - quantity);
        stock.setQuantityAvailable(stock.getQuantityAvailable() + quantity);
        stockRepository.save(stock);
    }

    @Transactional
    public void initializeMissingStocks() {
        List<Book> allBooks = bookRepository.findAll();

        Set<Long> bookIdsWithStock = stockRepository.findAll().stream()
                .map(stock -> stock.getBook().getId())
                .collect(Collectors.toSet());

        List<Stock> newStocks = new ArrayList<>();

        for (Book book : allBooks) {
            if (!bookIdsWithStock.contains(book.getId())) {
                Stock stock = new Stock();
                stock.setBook(book);
                stock.setQuantityAvailable(0);
                stock.setQuantityReserved(0);
                stock.setQuantitySold(0);
                newStocks.add(stock);
            }
        }

        stockRepository.saveAll(newStocks);
    }

    @Transactional(readOnly = true)
    public StockResponseDto getStockInfo(Long bookId) {
        Stock stock = stockRepository.findByBookId(bookId);
        if (stock == null) {
            throw new NotFoundException("Stock not found for book with id " + bookId);
        }
        return stockMapper.asOutput(stock);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<LowStockBookResponseDto> getLowStockBooks(int threshold, Pageable pageable) {
        Page<LowStockBookResponseDto> page = stockRepository.findLowStockBooks(threshold, pageable);
        return PageResponseDto.from(page);
    }
}
