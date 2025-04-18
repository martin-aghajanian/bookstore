package com.martin.bookstore.service;

import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.dto.request.AuthorRequestDto;
import com.martin.bookstore.dto.response.AuthorResponseDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.entity.Author;
import com.martin.bookstore.core.mapper.AuthorMapper;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.BookAuthor;
import com.martin.bookstore.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public AuthorResponseDto getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        Author saved = authorRepository.save(authorMapper.asEntity(dto));
        return authorMapper.asOutput(saved);
    }

    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        authorMapper.update(author, dto);
        return authorMapper.asOutput(authorRepository.save(author));
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        if (!author.getBookAuthor().isEmpty()) {
            throw new IllegalStateException("Cannot delete author: they are associated with books.");
        }

        authorRepository.delete(author);
    }

    public Page<AuthorResponseDto> filterAuthors(Boolean goodreads, String contribution, Pageable pageable) {
        return authorRepository.filterAuthors(goodreads, contribution, pageable)
                .map(authorMapper::asOutput);
    }

    public Page<AuthorResponseDto> searchAuthorsByName(String name, Pageable pageable) {
        return authorRepository.findByFullNameContainingIgnoreCase(name, pageable)
                .map(authorMapper::asOutput);
    }

    public Page<BookResponseDto> getBooksByAuthorId(Long authorId, Pageable pageable) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        List<Book> books = author.getBookAuthor().stream()
                .map(BookAuthor::getBook)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), books.size());

        List<BookResponseDto> content = books.subList(start, end).stream()
                .map(bookMapper::asOutput)
                .toList();

        return new PageImpl<>(content, pageable, books.size());
    }
}

