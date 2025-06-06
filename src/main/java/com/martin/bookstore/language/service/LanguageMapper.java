package com.martin.bookstore.language.service;

import com.martin.bookstore.language.dto.LanguageRequestDto;
import com.martin.bookstore.language.dto.LanguageResponseDto;
import com.martin.bookstore.language.persistence.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface LanguageMapper {

    Language asEntity(LanguageRequestDto dto);

    LanguageResponseDto asOutput(Language entity);

    List<LanguageResponseDto> asOutput(List<Language> entities);

    void update(@MappingTarget Language entity, LanguageRequestDto dto);
}
