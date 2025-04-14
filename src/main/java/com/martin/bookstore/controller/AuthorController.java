package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.AuthorRequestDto;
import com.martin.bookstore.dto.response.AuthorResponseDto;
import com.martin.bookstore.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public AuthorResponseDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public AuthorResponseDto createAuthor(@RequestBody AuthorRequestDto dto) {
        return authorService.createAuthor(dto);
    }

    @PutMapping("/{id}")
    public AuthorResponseDto updateAuthor(@PathVariable Long id, @RequestBody AuthorRequestDto dto) {
        return authorService.updateAuthor(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping
    public Page<AuthorResponseDto> filterAuthors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean goodreads,
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) String contribution,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return authorService.filterAuthors(name, goodreads, bookTitle, contribution, PageRequest.of(page, size));
    }
}
