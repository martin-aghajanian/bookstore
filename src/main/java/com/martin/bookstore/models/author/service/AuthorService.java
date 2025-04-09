package com.martin.bookstore.models.author.service;

import com.martin.bookstore.models.author.dto.AuthorDto;
import com.martin.bookstore.models.author.persistence.entity.Author;
import com.martin.bookstore.models.author.persistence.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<AuthorDto> getAuthorById(Long id) {
        return authorRepository.findById(id).map(this::toDto);
    }

    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author saved = authorRepository.save(toEntity(authorDto));
        return toDto(saved);
    }

    public AuthorDto updateAuthor(Long id, AuthorDto updatedAuthorDto) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setFullName(updatedAuthorDto.getFullName());
            author.setGoodReadsAuthor(updatedAuthorDto.getGoodReadsAuthor());
            return toDto(authorRepository.save(author));
        }
        return null;
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    private AuthorDto toDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setFullName(author.getFullName());
        dto.setGoodReadsAuthor(author.getGoodReadsAuthor());
        return dto;
    }

    private Author toEntity(AuthorDto dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setFullName(dto.getFullName());
        author.setGoodReadsAuthor(dto.getGoodReadsAuthor());
        return author;
    }
}
