package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.GenreDto;
import com.martin.bookstore.entity.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }

    public Genre toEntity(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;
    }
}
