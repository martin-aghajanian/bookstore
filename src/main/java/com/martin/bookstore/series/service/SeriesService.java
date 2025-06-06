package com.martin.bookstore.series.service;

import com.martin.bookstore.series.persistence.repository.SeriesRepository;
import com.martin.bookstore.series.dto.SeriesRequestDto;
import com.martin.bookstore.series.dto.SeriesResponseDto;
import com.martin.bookstore.series.persistence.entity.Series;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<SeriesResponseDto> getAllSeries() {
        return seriesMapper.asOutput(seriesRepository.findAll());
    }

    public SeriesResponseDto getSeriesById(Long id) {
        return seriesRepository.findById(id)
                .map(seriesMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("series with id " + id + " not found"));
    }

    public SeriesResponseDto createSeries(SeriesRequestDto dto) {
        Series saved = seriesRepository.save(seriesMapper.asEntity(dto));
        return seriesMapper.asOutput(saved);
    }

    public SeriesResponseDto updateSeries(Long id, SeriesRequestDto dto) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("series with id " + id + " not found"));
        seriesMapper.update(series, dto);
        return seriesMapper.asOutput(seriesRepository.save(series));
    }

    public void deleteSeries(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("series with id " + id + " not found"));

        if (series.getBooks() != null && !series.getBooks().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete series associated with books");
        }

        seriesRepository.delete(series);
    }

    public Page<BookResponseDto> getBooksBySeries(Long seriesId, Pageable pageable) {
        seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NotFoundException("series with id " + seriesId + " not found"));

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("publishDate").ascending()
        );

        return bookRepository.findBySeriesId(seriesId, sortedPageable)
                .map(bookMapper::asOutput);
    }
}
