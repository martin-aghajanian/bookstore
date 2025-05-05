package com.martin.bookstore.service;

import com.martin.bookstore.core.exception.DeleteNotAllowedException;
import com.martin.bookstore.core.exception.NotFoundException;
import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.FormatRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.FormatResponseDto;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.Format;
import com.martin.bookstore.core.mapper.FormatMapper;
import com.martin.bookstore.repository.BookRepository;
import com.martin.bookstore.repository.FormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
