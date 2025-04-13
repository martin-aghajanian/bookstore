package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.AuthorDto;
import com.martin.bookstore.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setFullName(author.getFullName());
        dto.setGoodReadsAuthor(author.getGoodReadsAuthor());
        return dto;
    }

    public Author toEntity(AuthorDto dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setFullName(dto.getFullName());
        author.setGoodReadsAuthor(dto.getGoodReadsAuthor());
        return author;
    }
}
