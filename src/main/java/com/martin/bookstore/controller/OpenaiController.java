package com.martin.bookstore.controller;

import com.martin.bookstore.dto.response.PageResponseDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.service.OpenaiService;
import com.martin.bookstore.service.ChatBookSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chat")
public class OpenaiController {

    private final OpenaiService openAIService;
    private final ChatBookSuggestionService suggestionService;

    @GetMapping("/suggest")
    @PreAuthorize("hasAnyAuthority('user:read', 'content:read', 'admin:read')")
    public PageResponseDto<BookResponseDto> suggestBooks(
            @RequestParam String message,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return suggestionService.suggestBooks(message, page, size);
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<String> test(
            @RequestParam(defaultValue = "hello") String prompt
    ) {
        String aiResponse = openAIService.getTestCompletion(prompt);
        return ResponseEntity.ok(aiResponse);
    }
}