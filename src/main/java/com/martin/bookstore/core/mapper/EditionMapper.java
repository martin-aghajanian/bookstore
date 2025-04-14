package com.martin.bookstore.core.mapper;

import com.martin.bookstore.entity.Edition;
import com.martin.bookstore.dto.request.EditionRequestDto;
import com.martin.bookstore.dto.response.EditionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface EditionMapper {

    Edition asEntity(EditionRequestDto dto);

    EditionResponseDto asOutput(Edition entity);

    List<EditionResponseDto> asOutput(List<Edition> entities);

    void update(@MappingTarget Edition entity, EditionRequestDto dto);
}