package com.martin.bookstore.service;

import com.martin.bookstore.dto.GenreDto;
import com.martin.bookstore.core.mapper.GenreMapper;
import com.martin.bookstore.entity.Genre;
import com.martin.bookstore.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream().map(genreMapper::toDto).collect(Collectors.toList());
    }

    public GenreDto getGenreById(Long id) {
        return genreRepository.findById(id).map(genreMapper::toDto).orElseThrow(() -> new RuntimeException("genre not found"));
    }

    public GenreDto createGenre(GenreDto genreDto) {
        Genre saved = genreRepository.save(genreMapper.toEntity(genreDto));
        return genreMapper.toDto(saved);
    }

    public GenreDto updateGenre(Long id, GenreDto updatedGenreDto) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
            genre.setName(updatedGenreDto.getName());
            return genreMapper.toDto(genreRepository.save(genre));
        }
        return null;
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
