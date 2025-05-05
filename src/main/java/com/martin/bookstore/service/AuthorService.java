package com.martin.bookstore.service;

import com.martin.bookstore.core.exception.DeleteNotAllowedException;
import com.martin.bookstore.core.exception.NotFoundException;
import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.criteria.AuthorSearchCriteria;
import com.martin.bookstore.dto.PageResponseDto;
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

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public AuthorResponseDto getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("Author with id " + id + " not found"));
    }

    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        Author saved = authorRepository.save(authorMapper.asEntity(dto));
        return authorMapper.asOutput(saved);
    }

    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id " + id + " not found"));
        authorMapper.update(author, dto);
        return authorMapper.asOutput(authorRepository.save(author));
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id " + id + " not found"));

        if (!author.getBookAuthor().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete author: they are associated with books.");
        }

        authorRepository.delete(author);
    }

    public Page<BookResponseDto> getBooksByAuthorId(Long authorId, Pageable pageable) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id " + authorId + " not found"));

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

    public PageResponseDto<AuthorResponseDto> getAll(AuthorSearchCriteria criteria) {
        Page<AuthorResponseDto> page = authorRepository.findAll(
                criteria,
                criteria.buildPageRequest()
        );
        return PageResponseDto.from(page);
    }
}

