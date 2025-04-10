package com.martin.bookstore.models.author.controller;

import com.martin.bookstore.models.author.dto.AuthorDto;
import com.martin.bookstore.models.author.service.AuthorService;
import com.martin.bookstore.models.author.service.AuthorServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
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

    @GetMapping("/search")
    public List<AuthorDto> searchAuthors(@RequestParam String name) {
        return authorService.searchAuthorsByName(name);
    }

    @GetMapping("/filter")
    public List<AuthorDto> filterByGoodreads(@RequestParam boolean goodreads) {
        return authorService.filterByGoodreadsAuthor(goodreads);
    }

    @GetMapping("/paginated")
    public Page<AuthorDto> getAuthorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return authorService.getAuthorsPaginated(PageRequest.of(page, size));
    }

}
