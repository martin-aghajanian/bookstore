package com.martin.bookstore.models.language.service;


import com.martin.bookstore.models.language.dto.LanguageDto;

import java.util.List;

public interface LanguageService {

    List<LanguageDto> getAllLanguages();

    LanguageDto getLanguageById(Long id);

    LanguageDto createLanguage(LanguageDto languageDto);

    LanguageDto updateLanguage(Long id, LanguageDto updateLanguageDto);

    void deleteLanguage(Long id);
}
