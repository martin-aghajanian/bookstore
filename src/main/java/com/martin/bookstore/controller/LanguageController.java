package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.LanguageRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.LanguageResponseDto;
import com.martin.bookstore.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LanguageResponseDto> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LanguageResponseDto getLanguageById(@PathVariable Long id) {
        return languageService.getLanguageById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LanguageResponseDto createLanguage(@RequestBody LanguageRequestDto dto) {
        return languageService.createLanguage(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LanguageResponseDto updateLanguage(@PathVariable Long id, @RequestBody LanguageRequestDto dto) {
        return languageService.updateLanguage(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookResponseDto> getBooksByLanguage(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return languageService.getBooksByLanguage(id, pageable);
    }
}
