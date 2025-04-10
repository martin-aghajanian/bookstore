package com.martin.bookstore.models.series.controller;

import com.martin.bookstore.models.series.dto.SeriesDto;
import com.martin.bookstore.models.series.service.SeriesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SeriesController {

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @GetMapping
    public List<SeriesDto> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/{id}")
    public SeriesDto getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }

    @PostMapping
    public SeriesDto createSeries(@RequestBody SeriesDto seriesDto) {
        return seriesService.createSeries(seriesDto);
    }

    @PutMapping("/{id}")
    public SeriesDto updateSeries(@PathVariable Long id, @RequestBody SeriesDto seriesDto) {
        return seriesService.updateSeries(id, seriesDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
    }
}
