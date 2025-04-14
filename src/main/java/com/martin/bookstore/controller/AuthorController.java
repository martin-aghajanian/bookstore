package com.martin.bookstore.controller;

import com.martin.bookstore.dto.old.AuthorDto;
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
    public AuthorDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public AuthorDto createAuthor(@RequestBody AuthorDto authorDto) {
        return authorService.createAuthor(authorDto);
    }

    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable Long id, @RequestBody AuthorDto authorDto) {
        return authorService.updateAuthor(id, authorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping
    public Page<AuthorDto> filterAuthors(
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
