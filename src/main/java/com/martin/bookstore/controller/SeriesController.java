package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.SeriesRequestDto;
import com.martin.bookstore.dto.response.SeriesResponseDto;
import com.martin.bookstore.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping
    public List<SeriesResponseDto> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/{id}")
    public SeriesResponseDto getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }

    @PostMapping
    public SeriesResponseDto createSeries(@RequestBody SeriesRequestDto dto) {
        return seriesService.createSeries(dto);
    }

    @PutMapping("/{id}")
    public SeriesResponseDto updateSeries(@PathVariable Long id, @RequestBody SeriesRequestDto dto) {
        return seriesService.updateSeries(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
    }
}
