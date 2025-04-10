package com.martin.bookstore.models.language.dto;

import com.martin.bookstore.models.language.persistence.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public LanguageDto toDto(Language language) {
        LanguageDto languageDto = new LanguageDto();
        languageDto.setId(language.getId());
        languageDto.setName(language.getName());
        return languageDto;
    }

    public Language toEntity(LanguageDto languageDto) {
        Language language = new Language();
        language.setId(languageDto.getId());
        language.setName(languageDto.getName());
        return language;
    }

}
