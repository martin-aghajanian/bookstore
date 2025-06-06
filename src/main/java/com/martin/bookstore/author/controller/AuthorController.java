package com.martin.bookstore.author.controller;

import com.martin.bookstore.author.criteria.AuthorSearchCriteria;
import com.martin.bookstore.author.service.AuthorService;
import com.martin.bookstore.author.dto.AuthorRequestDto;
import com.martin.bookstore.author.dto.AuthorResponseDto;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorResponseDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDto createAuthor(@RequestBody AuthorRequestDto dto) {
        return authorService.createAuthor(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorResponseDto updateAuthor(@PathVariable Long id, @RequestBody AuthorRequestDto dto) {
        return authorService.updateAuthor(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<AuthorResponseDto> getAll(AuthorSearchCriteria criteria) {
        return authorService.getAll(criteria);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getBooksByAuthor(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return authorService.getBooksByAuthorId(id, page, size);
    }

}
