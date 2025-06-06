package com.martin.bookstore.book.controller;

import com.martin.bookstore.author.dto.AuthorResponseDto;
import com.martin.bookstore.award.dto.AwardResponseDto;
import com.martin.bookstore.book.dto.BookRequestDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.criteria.BookSearchCriteria;
import com.martin.bookstore.book.service.BookService;
import com.martin.bookstore.character.dto.CharacterResponseDto;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.genre.dto.GenreResponseDto;
import com.martin.bookstore.setting.dto.SettingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto createBook(@RequestBody BookRequestDto dto) {
        return bookService.createBook(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto updateBook(@PathVariable Long id, @RequestBody BookRequestDto dto) {
        return bookService.updateBook(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getAll(BookSearchCriteria criteria) {
        return bookService.getAll(criteria);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/genres")
    @ResponseStatus(HttpStatus.OK)
    public List<GenreResponseDto> getGenresByBook(@PathVariable Long id) {
        return bookService.getGenresByBookId(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/authors")
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorResponseDto> getAuthorsByBook(@PathVariable Long id) {
        return bookService.getAuthorsByBookId(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.OK)
    public List<SettingResponseDto> getSettingsByBook(@PathVariable Long id) {
        return bookService.getSettingsByBookId(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/awards")
    @ResponseStatus(HttpStatus.OK)
    public List<AwardResponseDto> getAwardsByBook(@PathVariable Long id) {
        return bookService.getAwardsByBookId(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/characters")
    @ResponseStatus(HttpStatus.OK)
    public List<CharacterResponseDto> getCharactersByBook(@PathVariable Long id) {
        return bookService.getCharactersByBookId(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/similar")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponseDto> getSimilarBooks(@PathVariable Long id) {
        return bookService.findSimilarBooks(id);
    }
}
