package com.martin.bookstore.controller;

import com.martin.bookstore.criteria.BookSearchCriteria;
import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.filters.BookFilterRequestDto;
import com.martin.bookstore.dto.request.BookRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto createBook(@RequestBody BookRequestDto dto) {
        return bookService.createBook(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto updateBook(@PathVariable Long id, @RequestBody BookRequestDto dto) {
        return bookService.updateBook(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getAll(BookSearchCriteria criteria) {
        return bookService.getAll(criteria);
    }


}
