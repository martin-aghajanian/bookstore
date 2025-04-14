package com.martin.bookstore.controller;

import com.martin.bookstore.dto.old.GenreDto;
import com.martin.bookstore.service.GenreService;
import com.martin.bookstore.repository.GenreRepository;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreRepository genreRepository;
    private final PathMatcher pathMatcher;

    public GenreController(GenreService genreService, GenreRepository genreRepository, PathMatcher pathMatcher) {
        this.genreService = genreService;
        this.genreRepository = genreRepository;
        this.pathMatcher = pathMatcher;
    }

    @GetMapping
    public List<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PostMapping
    public GenreDto createGenre(@RequestBody GenreDto genreDto) {
        return genreService.createGenre(genreDto);
    }

    @PutMapping("/{id}")
    public GenreDto updateGenre(@PathVariable Long id, @RequestBody GenreDto genreDto) {
        return genreService.updateGenre(id, genreDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }


}
