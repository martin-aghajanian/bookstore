package com.martin.bookstore.openai.service;

import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBookSuggestionService {

    private final OpenaiService openaiService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public PageResponseDto<BookResponseDto> suggestBooks(String userMessage, int page, int size) {
        List<String> titles = openaiService.getSuggestedTitles(userMessage);

        if (titles.isEmpty()) {
            return PageResponseDto.empty(page, size);
        }

        Set<String> cleanTitles = titles.stream()
                .map(title -> title.trim().toLowerCase())
                .collect(Collectors.toSet());

        PageRequest pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByTitleInIgnoreCase(cleanTitles, pageable);

        Page<BookResponseDto> dtoPage = bookPage.map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }
}
