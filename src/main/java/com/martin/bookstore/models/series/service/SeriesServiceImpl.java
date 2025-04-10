package com.martin.bookstore.models.series.service;

import com.martin.bookstore.models.series.dto.SeriesDto;
import com.martin.bookstore.models.series.dto.SeriesMapper;
import com.martin.bookstore.models.series.persistence.entity.Series;
import com.martin.bookstore.models.series.persistence.repository.SeriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesServiceImpl implements SeriesService{

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    public SeriesServiceImpl(SeriesRepository seriesRepository, SeriesMapper seriesMapper) {
        this.seriesRepository = seriesRepository;
        this.seriesMapper = seriesMapper;
    }

    @Override
    public List<SeriesDto> getAllSeries() {
        return seriesRepository.findAll().stream().map(seriesMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SeriesDto getSeriesById(Long id) {
        return seriesRepository.findById(id).map(seriesMapper::toDto).orElseThrow(() -> new RuntimeException("series not found"));
    }

    @Override
    public SeriesDto createSeries(SeriesDto seriesDto) {
        Series saved = seriesRepository.save(seriesMapper.toEntity(seriesDto));
        return seriesMapper.toDto(saved);
    }

    @Override
    public SeriesDto updateSeries(Long id, SeriesDto updatedSeriesDto) {
        Optional<Series> optionalSeries = seriesRepository.findById(id);
        if (optionalSeries.isPresent()) {
            Series series = optionalSeries.get();
            series.setName(updatedSeriesDto.getName());
            return seriesMapper.toDto(seriesRepository.save(series));
        }
        return null;
    }

    @Override
    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }
}
