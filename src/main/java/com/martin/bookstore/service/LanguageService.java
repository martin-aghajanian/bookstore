package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.LanguageRequestDto;
import com.martin.bookstore.dto.response.LanguageResponseDto;
import com.martin.bookstore.entity.Language;
import com.martin.bookstore.core.mapper.LanguageMapper;
import com.martin.bookstore.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    public List<LanguageResponseDto> getAllLanguages() {
        return languageMapper.asOutput(languageRepository.findAll());
    }

    public LanguageResponseDto getLanguageById(Long id) {
        return languageRepository.findById(id)
                .map(languageMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("language not found"));
    }

    public LanguageResponseDto createLanguage(LanguageRequestDto dto) {
        Language saved = languageRepository.save(languageMapper.asEntity(dto));
        return languageMapper.asOutput(saved);
    }

    public LanguageResponseDto updateLanguage(Long id, LanguageRequestDto dto) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("language not found"));
        languageMapper.update(language, dto);
        return languageMapper.asOutput(languageRepository.save(language));
    }

    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
