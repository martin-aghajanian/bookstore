package com.martin.bookstore.author.service;

import com.martin.bookstore.author.dto.AuthorRequestDto;
import com.martin.bookstore.author.dto.AuthorResponseDto;
import com.martin.bookstore.author.persistence.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author asEntity(AuthorRequestDto authorRequest);

    AuthorResponseDto asOutput(Author author);

    List<AuthorResponseDto> asOutput(List<Author> authors);

    void update(@MappingTarget Author entity, AuthorRequestDto authorRequest);
}