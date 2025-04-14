package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.AuthorRequestDto;
import com.martin.bookstore.dto.response.AuthorResponseDto;
import com.martin.bookstore.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

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
            @RequestParam(required = false) Boolean goodreads,
            @RequestParam(required = false) String contribution,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return authorService.filterAuthors(goodreads, contribution, PageRequest.of(page, size));
    }

    @GetMapping("/search")
    public Page<AuthorResponseDto> searchAuthors(@RequestParam String name,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return authorService.searchAuthorsByName(name, PageRequest.of(page, size));
    }

}
