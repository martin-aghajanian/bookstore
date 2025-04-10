package com.martin.bookstore.models.series.service;

import com.martin.bookstore.models.series.dto.SeriesDto;

import java.util.List;

public interface SeriesService {

    List<SeriesDto> getAllSeries();

    SeriesDto getSeriesById(Long id);

    SeriesDto createSeries(SeriesDto seriesDto);

    SeriesDto updateSeries(Long id, SeriesDto updatedSeriesDto);

    void deleteSeries(Long id);

}
