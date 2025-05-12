package com.martin.bookstore.openai;

import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import org.springframework.http.ResponseEntity;
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
    public PageResponseDto<BookResponseDto> suggestBooks(
            @RequestParam String message,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return suggestionService.suggestBooks(message, page, size);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(
            @RequestParam(defaultValue = "Hello from the Bookstore!") String prompt
    ) {
        String aiResponse = openAIService.getTestCompletion(prompt);
        return ResponseEntity.ok(aiResponse);
    }
}