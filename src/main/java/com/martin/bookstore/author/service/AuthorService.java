package com.martin.bookstore.author.service;

import com.martin.bookstore.author.criteria.AuthorSearchCriteria;
import com.martin.bookstore.author.dto.AuthorRequestDto;
import com.martin.bookstore.author.dto.AuthorResponseDto;
import com.martin.bookstore.author.persistence.entity.Author;
import com.martin.bookstore.author.persistence.repository.AuthorRepository;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.repository.BookAuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;
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

    public PageResponseDto<BookResponseDto> getBooksByAuthorId(Long authorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> bookPage = bookAuthorRepository.findBooksByAuthorId(authorId, pageRequest);
        Page<BookResponseDto> dtoPage = bookPage.map(bookMapper::asOutput);
        return PageResponseDto.from(dtoPage);
    }

    public PageResponseDto<AuthorResponseDto> getAll(AuthorSearchCriteria criteria) {
        Page<AuthorResponseDto> page = authorRepository.findAll(
                criteria,
                criteria.buildPageRequest()
        );
        return PageResponseDto.from(page);
    }
}

