package com.martin.bookstore.models.genre.service;

import com.martin.bookstore.models.genre.dto.GenreDto;
import com.martin.bookstore.models.genre.dto.GenreMapper;
import com.martin.bookstore.models.genre.persistence.entity.Genre;
import com.martin.bookstore.models.genre.persistence.repository.GenreRepository;
import lombok.Setter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl  implements GenreService{

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream().map(genreMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public GenreDto getGenreById(Long id) {
        return genreRepository.findById(id).map(genreMapper::toDto).orElseThrow(() -> new RuntimeException("genre not found"));
    }

    @Override
    public GenreDto createGenre(GenreDto genreDto) {
        Genre saved = genreRepository.save(genreMapper.toEntity(genreDto));
        return genreMapper.toDto(saved);
    }

    @Override
    public GenreDto updateGenre(Long id, GenreDto updatedGenreDto) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
            genre.setName(updatedGenreDto.getName());
            return genreMapper.toDto(genreRepository.save(genre));
        }
        return null;
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
