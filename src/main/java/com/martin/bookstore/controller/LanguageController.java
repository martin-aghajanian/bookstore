package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.LanguageRequestDto;
import com.martin.bookstore.dto.response.LanguageResponseDto;
import com.martin.bookstore.service.LanguageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<LanguageResponseDto> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/{id}")
    public LanguageResponseDto getLanguageById(@PathVariable Long id) {
        return languageService.getLanguageById(id);
    }

    @PostMapping
    public LanguageResponseDto createLanguage(@RequestBody LanguageRequestDto dto) {
        return languageService.createLanguage(dto);
    }

    @PutMapping("/{id}")
    public LanguageResponseDto updateLanguage(@PathVariable Long id, @RequestBody LanguageRequestDto dto) {
        return languageService.updateLanguage(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
    }
}
