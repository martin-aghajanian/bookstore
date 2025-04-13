package com.martin.bookstore.service;

import com.martin.bookstore.dto.AuthorDto;
import com.martin.bookstore.core.mapper.AuthorMapper;
import com.martin.bookstore.entity.Author;
import com.martin.bookstore.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public AuthorDto getAuthorById(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto).orElseThrow(() -> new RuntimeException("author not found"));
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

    public List<AuthorDto> searchAuthorsByName(String name) {
        return authorRepository.findByFullNameContainingIgnoreCase(name)
                .stream().map(authorMapper::toDto).collect(Collectors.toList());
    }

    public List<AuthorDto> filterByGoodreadsAuthor(boolean goodreads) {
        return authorRepository.findByGoodReadsAuthor(goodreads)
                .stream().map(authorMapper::toDto).collect(Collectors.toList());
    }

    public Page<AuthorDto> filterAuthors(String name, Boolean goodreads, String bookTitle, String contribution, Pageable pageable) {
        return authorRepository.filterAuthors(Optional.ofNullable(name).orElse(""), goodreads, bookTitle, contribution, pageable)
                .map(authorMapper::toDto);
    }


}
