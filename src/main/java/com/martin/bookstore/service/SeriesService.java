package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.SeriesRequestDto;
import com.martin.bookstore.dto.response.SeriesResponseDto;
import com.martin.bookstore.entity.Series;
import com.martin.bookstore.core.mapper.SeriesMapper;
import com.martin.bookstore.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    public List<SeriesResponseDto> getAllSeries() {
        return seriesMapper.asOutput(seriesRepository.findAll());
    }

    public SeriesResponseDto getSeriesById(Long id) {
        return seriesRepository.findById(id)
                .map(seriesMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("series not found"));
    }

    public SeriesResponseDto createSeries(SeriesRequestDto dto) {
        Series saved = seriesRepository.save(seriesMapper.asEntity(dto));
        return seriesMapper.asOutput(saved);
    }

    public SeriesResponseDto updateSeries(Long id, SeriesRequestDto dto) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("series not found"));
        seriesMapper.update(series, dto);
        return seriesMapper.asOutput(seriesRepository.save(series));
    }

    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }
}
