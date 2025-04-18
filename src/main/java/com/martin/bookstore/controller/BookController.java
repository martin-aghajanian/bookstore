package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.BookRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookResponseDto createBook(@RequestBody BookRequestDto dto) {
        return bookService.createBook(dto);
    }

    @PutMapping("/{id}")
    public BookResponseDto updateBook(@PathVariable Long id, @RequestBody BookRequestDto dto) {
        return bookService.updateBook(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/search")
    public Page<BookResponseDto> searchBooks(
            @RequestParam("title") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookService.searchBooksByTitleOrDescription(query, PageRequest.of(page, size));
    }


}
