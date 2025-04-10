package com.martin.bookstore.models.genre.service;

import com.martin.bookstore.models.genre.dto.GenreDto;

import java.util.List;

public interface GenreService {

    List<GenreDto> getAllGenres();

    GenreDto getGenreById(Long id);

    GenreDto createGenre(GenreDto genreDto);

    GenreDto updateGenre(Long id, GenreDto updatedGenreDto);

    void deleteGenre(Long id);
}
