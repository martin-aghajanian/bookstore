package com.martin.bookstore.language.controller;

import com.martin.bookstore.language.dto.LanguageRequestDto;
import com.martin.bookstore.language.dto.LanguageResponseDto;
import com.martin.bookstore.language.service.LanguageService;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LanguageResponseDto> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LanguageResponseDto getLanguageById(@PathVariable Long id) {
        return languageService.getLanguageById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LanguageResponseDto createLanguage(@RequestBody LanguageRequestDto dto) {
        return languageService.createLanguage(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LanguageResponseDto updateLanguage(@PathVariable Long id, @RequestBody LanguageRequestDto dto) {
        return languageService.updateLanguage(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getBooksByLanguage(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return languageService.getBooksByLanguage(id, page, size);
    }
}
