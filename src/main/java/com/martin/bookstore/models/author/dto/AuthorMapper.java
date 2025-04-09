package com.martin.bookstore.models.author.dto;

import com.martin.bookstore.models.author.persistence.entity.Author;
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
