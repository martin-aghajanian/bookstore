package com.martin.bookstore.genre.service;

import com.martin.bookstore.genre.dto.GenreRequestDto;
import com.martin.bookstore.genre.dto.GenreResponseDto;
import com.martin.bookstore.genre.persistence.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre asEntity(GenreRequestDto dto);

    GenreResponseDto asOutput(Genre entity);

    List<GenreResponseDto> asOutput(List<Genre> entities);

    void update(@MappingTarget Genre entity, GenreRequestDto dto);
}
