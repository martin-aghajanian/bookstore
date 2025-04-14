package com.martin.bookstore.service;

import com.martin.bookstore.dto.old.LanguageDto;
import com.martin.bookstore.core.mapper.old.LanguageMapper;
import com.martin.bookstore.entity.Language;
import com.martin.bookstore.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAll().stream().map(languageMapper::toDto).collect(Collectors.toList());
    }

    public LanguageDto getLanguageById(Long id) {
        return languageRepository.findById(id).map(languageMapper::toDto).orElseThrow(() -> new RuntimeException("language not found"));
    }

    public LanguageDto createLanguage(LanguageDto languageDto) {
        Language saved = languageRepository.save(languageMapper.toEntity(languageDto));
        return languageMapper.toDto(saved);
    }

    public LanguageDto updateLanguage(Long id, LanguageDto updateLanguageDto) {
        Optional<Language> optionalLanguage = languageRepository.findById(id);
        if (optionalLanguage.isPresent()) {
            Language language = optionalLanguage.get();
            language.setName(updateLanguageDto.getName());
            return languageMapper.toDto(languageRepository.save(language));
        }
        return null;
    }

    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
