package com.martin.bookstore.service;

import com.martin.bookstore.dto.SeriesDto;
import com.martin.bookstore.core.mapper.SeriesMapper;
import com.martin.bookstore.entity.Series;
import com.martin.bookstore.repository.SeriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    public SeriesService(SeriesRepository seriesRepository, SeriesMapper seriesMapper) {
        this.seriesRepository = seriesRepository;
        this.seriesMapper = seriesMapper;
    }

    public List<SeriesDto> getAllSeries() {
        return seriesRepository.findAll().stream().map(seriesMapper::toDto).collect(Collectors.toList());
    }

    public SeriesDto getSeriesById(Long id) {
        return seriesRepository.findById(id).map(seriesMapper::toDto).orElseThrow(() -> new RuntimeException("series not found"));
    }

    public SeriesDto createSeries(SeriesDto seriesDto) {
        Series saved = seriesRepository.save(seriesMapper.toEntity(seriesDto));
        return seriesMapper.toDto(saved);
    }

    public SeriesDto updateSeries(Long id, SeriesDto updatedSeriesDto) {
        Optional<Series> optionalSeries = seriesRepository.findById(id);
        if (optionalSeries.isPresent()) {
            Series series = optionalSeries.get();
            series.setName(updatedSeriesDto.getName());
            return seriesMapper.toDto(seriesRepository.save(series));
        }
        return null;
    }

    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }
}
