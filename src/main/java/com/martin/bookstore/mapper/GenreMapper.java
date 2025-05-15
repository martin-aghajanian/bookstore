package com.martin.bookstore.mapper;

import com.martin.bookstore.entity.Genre;
import com.martin.bookstore.dto.request.GenreRequestDto;
import com.martin.bookstore.dto.response.GenreResponseDto;
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
