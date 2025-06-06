package com.martin.bookstore.format.service;

import com.martin.bookstore.format.dto.FormatRequestDto;
import com.martin.bookstore.format.dto.FormatResponseDto;
import com.martin.bookstore.format.persistence.entity.Format;
import com.martin.bookstore.format.persistence.repository.FormatRepository;
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
public class FormatService {

    private final FormatRepository formatRepository;
    private final FormatMapper formatMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<FormatResponseDto> getAllFormats() {
        return formatMapper.asOutput(formatRepository.findAll());
    }

    public FormatResponseDto getFormatById(Long id) {
        return formatRepository.findById(id)
                .map(formatMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("format with id " + id + " not found"));
    }

    public FormatResponseDto createFormat(FormatRequestDto dto) {
        Format saved = formatRepository.save(formatMapper.asEntity(dto));
        return formatMapper.asOutput(saved);
    }

    public FormatResponseDto updateFormat(Long id, FormatRequestDto dto) {
        Format format = formatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("format with id " + id + " not found"));
        formatMapper.update(format, dto);
        return formatMapper.asOutput(formatRepository.save(format));
    }

    public void deleteFormat(Long id) {
        Format format = formatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("format with id " + id + " not found"));

        if (format.getBooks() != null && !format.getBooks().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete format associated with books");
        }

        formatRepository.delete(format);
    }

    public PageResponseDto<BookResponseDto> getBooksByFormat(Long formatId, int page, int size) {
        formatRepository.findById(formatId)
                .orElseThrow(() -> new NotFoundException("format with id " + formatId + " not found"));

        var pageRequest = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByFormatId(formatId, pageRequest);
        Page<BookResponseDto> dtoPage = bookPage.map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }
}
