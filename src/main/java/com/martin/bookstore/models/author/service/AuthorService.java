package com.martin.bookstore.models.author.service;

import com.martin.bookstore.models.author.dto.AuthorDto;
import com.martin.bookstore.models.author.dto.AuthorMapper;
import com.martin.bookstore.models.author.persistence.entity.Author;
import com.martin.bookstore.models.author.persistence.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(authorMapper::toDto).collect(Collectors.toList());
    }

    public Optional<AuthorDto> getAuthorById(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author saved = authorRepository.save(authorMapper.toEntity(authorDto));
        return authorMapper.toDto(saved);
    }

    public AuthorDto updateAuthor(Long id, AuthorDto updatedAuthorDto) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setFullName(updatedAuthorDto.getFullName());
            author.setGoodReadsAuthor(updatedAuthorDto.getGoodReadsAuthor());
            return authorMapper.toDto(authorRepository.save(author));
        }
        return null;
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }


}
