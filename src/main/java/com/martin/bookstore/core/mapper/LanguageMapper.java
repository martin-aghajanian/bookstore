package com.martin.bookstore.core.mapper;

import com.martin.bookstore.entity.Language;
import com.martin.bookstore.dto.request.LanguageRequestDto;
import com.martin.bookstore.dto.response.LanguageResponseDto;
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
