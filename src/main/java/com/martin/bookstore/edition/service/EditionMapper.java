package com.martin.bookstore.edition.service;

import com.martin.bookstore.edition.dto.EditionRequestDto;
import com.martin.bookstore.edition.dto.EditionResponseDto;
import com.martin.bookstore.edition.persistence.entity.Edition;
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