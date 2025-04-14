package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.GenreRequestDto;
import com.martin.bookstore.dto.response.GenreResponseDto;
import com.martin.bookstore.service.GenreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<GenreResponseDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreResponseDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PostMapping
    public GenreResponseDto createGenre(@RequestBody GenreRequestDto dto) {
        return genreService.createGenre(dto);
    }

    @PutMapping("/{id}")
    public GenreResponseDto updateGenre(@PathVariable Long id, @RequestBody GenreRequestDto dto) {
        return genreService.updateGenre(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }
}
