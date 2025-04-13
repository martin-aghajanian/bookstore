package com.martin.bookstore.controller;

import com.martin.bookstore.dto.LanguageDto;
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
    public List<LanguageDto> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/{id}")
    public LanguageDto getLanguageById(@PathVariable Long id) {
        return languageService.getLanguageById(id);
    }

    @PostMapping
    public LanguageDto createLanguage(@RequestBody LanguageDto languageDto) {
        return languageService.createLanguage(languageDto);
    }

    @PutMapping("/{id}")
    public LanguageDto updateLanguage(@PathVariable Long id, @RequestBody LanguageDto languageDto) {
        return languageService.updateLanguage(id, languageDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
    }
}
