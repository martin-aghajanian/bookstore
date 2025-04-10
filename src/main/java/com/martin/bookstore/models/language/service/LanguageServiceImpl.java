package com.martin.bookstore.models.language.service;

import com.martin.bookstore.models.language.dto.LanguageDto;
import com.martin.bookstore.models.language.dto.LanguageMapper;
import com.martin.bookstore.models.language.persistence.entity.Language;
import com.martin.bookstore.models.language.persistence.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LanguageServiceImpl implements LanguageService{

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    @Override
    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAll().stream().map(languageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public LanguageDto getLanguageById(Long id) {
        return languageRepository.findById(id).map(languageMapper::toDto).orElseThrow(() -> new RuntimeException("language not found"));
    }

    @Override
    public LanguageDto createLanguage(LanguageDto languageDto) {
        Language saved = languageRepository.save(languageMapper.toEntity(languageDto));
        return languageMapper.toDto(saved);
    }

    @Override
    public LanguageDto updateLanguage(Long id, LanguageDto updateLanguageDto) {
        Optional<Language> optionalLanguage = languageRepository.findById(id);
        if (optionalLanguage.isPresent()) {
            Language language = optionalLanguage.get();
            language.setName(updateLanguageDto.getName());
            return languageMapper.toDto(languageRepository.save(language));
        }
        return null;
    }

    @Override
    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
