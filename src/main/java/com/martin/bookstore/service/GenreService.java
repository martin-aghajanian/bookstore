package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.GenreRequestDto;
import com.martin.bookstore.dto.response.GenreResponseDto;
import com.martin.bookstore.entity.Genre;
import com.martin.bookstore.core.mapper.GenreMapper;
import com.martin.bookstore.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<GenreResponseDto> getAllGenres() {
        return genreMapper.asOutput(genreRepository.findAll());
    }

    public GenreResponseDto getGenreById(Long id) {
        return genreRepository.findById(id)
                .map(genreMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("genre not found"));
    }

    public GenreResponseDto createGenre(GenreRequestDto dto) {
        Genre saved = genreRepository.save(genreMapper.asEntity(dto));
        return genreMapper.asOutput(saved);
    }

    public GenreResponseDto updateGenre(Long id, GenreRequestDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("genre not found"));
        genreMapper.update(genre, dto);
        return genreMapper.asOutput(genreRepository.save(genre));
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
