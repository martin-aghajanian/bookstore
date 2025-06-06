package com.martin.bookstore.language.service;

import com.martin.bookstore.language.dto.LanguageRequestDto;
import com.martin.bookstore.language.dto.LanguageResponseDto;
import com.martin.bookstore.language.persistence.entity.Language;
import com.martin.bookstore.language.persistence.repository.LanguageRepository;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
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

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<LanguageResponseDto> getAllLanguages() {
        return languageMapper.asOutput(languageRepository.findAll());
    }

    public LanguageResponseDto getLanguageById(Long id) {
        return languageRepository.findById(id)
                .map(languageMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("language with id " + id + " not found"));
    }

    public LanguageResponseDto createLanguage(LanguageRequestDto dto) {
        Language saved = languageRepository.save(languageMapper.asEntity(dto));
        return languageMapper.asOutput(saved);
    }

    public LanguageResponseDto updateLanguage(Long id, LanguageRequestDto dto) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("language with id " + id + " not found"));
        languageMapper.update(language, dto);
        return languageMapper.asOutput(languageRepository.save(language));
    }

    public void deleteLanguage(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("language with id " + id + " not found"));

        if (language.getBooks() != null && !language.getBooks().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete language associated with books");
        }

        languageRepository.delete(language);
    }

    public PageResponseDto<BookResponseDto> getBooksByLanguage(Long languageId, int page, int size) {
        languageRepository.findById(languageId)
                .orElseThrow(() -> new NotFoundException("language with id " + languageId + " not found"));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByLanguageId(languageId, pageRequest);
        Page<BookResponseDto> dtoPage = bookPage.map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }
}
