package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.SeriesRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.SeriesResponseDto;
import com.martin.bookstore.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SeriesResponseDto> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SeriesResponseDto getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeriesResponseDto createSeries(@RequestBody SeriesRequestDto dto) {
        return seriesService.createSeries(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SeriesResponseDto updateSeries(@PathVariable Long id, @RequestBody SeriesRequestDto dto) {
        return seriesService.updateSeries(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookResponseDto> getBooksBySeries(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return seriesService.getBooksBySeries(id, pageable);
    }
}
