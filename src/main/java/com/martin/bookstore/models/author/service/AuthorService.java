package com.martin.bookstore.models.author.service;

import com.martin.bookstore.models.author.dto.AuthorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    List<AuthorDto> getAllAuthors();

    AuthorDto getAuthorById(Long id);

    AuthorDto createAuthor(AuthorDto authorDto);

    AuthorDto updateAuthor(Long id, AuthorDto authorDto);

    void deleteAuthor(Long id);

    List<AuthorDto> searchAuthorsByName(String name);

    List<AuthorDto> filterByGoodreadsAuthor(boolean goodreads);

//    Page<AuthorDto> getAuthorsPaginated(Pageable pageable);

    Page<AuthorDto> filterAuthors(String name, Boolean goodreads, String bookTitle, String contribution, Pageable pageable);
}
