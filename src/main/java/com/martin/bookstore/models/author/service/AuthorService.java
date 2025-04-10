package com.martin.bookstore.models.author.service;

import com.martin.bookstore.models.author.dto.AuthorDto;
import com.martin.bookstore.models.book.dto.BookDto;

import java.util.List;

public interface AuthorService {

    List<AuthorDto> getAllAuthors();

    AuthorDto getAuthorById(Long id);

    AuthorDto createAuthor(AuthorDto authorDto);

    AuthorDto updateAuthor(Long id, AuthorDto authorDto);

    void deleteAuthor(Long id);
}
