package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.SeriesDto;
import com.martin.bookstore.entity.Series;
import org.springframework.stereotype.Component;

@Component
public class SeriesMapper {

    public SeriesDto toDto(Series series) {
        SeriesDto seriesDto = new SeriesDto();
        seriesDto.setId(series.getId());
        seriesDto.setName(series.getName());
        return seriesDto;
    }

    public Series toEntity(SeriesDto seriesDto) {
        Series series = new Series();
        series.setId(seriesDto.getId());
        series.setName(seriesDto.getName());
        return series;
    }
}
