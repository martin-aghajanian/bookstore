package com.martin.bookstore.service;

import com.martin.bookstore.exception.DeleteNotAllowedException;
import com.martin.bookstore.exception.NotFoundException;
import com.martin.bookstore.mapper.BookMapper;
import com.martin.bookstore.dto.response.PageResponseDto;
import com.martin.bookstore.dto.request.GenreRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.GenreResponseDto;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.Genre;
import com.martin.bookstore.mapper.GenreMapper;
import com.martin.bookstore.repository.BookGenreRepository;
import com.martin.bookstore.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final BookGenreRepository bookGenreRepository;
    private final GenreMapper genreMapper;
    private final BookMapper bookMapper;

    public List<GenreResponseDto> getAllGenres() {
        return genreMapper.asOutput(genreRepository.findAll());
    }

    public GenreResponseDto getGenreById(Long id) {
        return genreRepository.findById(id)
                .map(genreMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("genre with id " + id + " not found"));
    }

    public GenreResponseDto createGenre(GenreRequestDto dto) {
        Genre saved = genreRepository.save(genreMapper.asEntity(dto));
        return genreMapper.asOutput(saved);
    }

    public GenreResponseDto updateGenre(Long id, GenreRequestDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("genre with id " + id + " not found"));
        genreMapper.update(genre, dto);
        return genreMapper.asOutput(genreRepository.save(genre));
    }

    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("genre with id " + id + " not found"));

        if (genre.getBookGenres() != null && !genre.getBookGenres().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete genre associated with books");
        }

        genreRepository.delete(genre);
    }


    public PageResponseDto<BookResponseDto> getBooksByGenre(Long genreId, int page, int size) {
        genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("genre with id " + genreId + " not found"));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> bookPage = bookGenreRepository.findBooksByGenreId(genreId, pageRequest);
        Page<BookResponseDto> dtoPage = bookPage.map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }
}
