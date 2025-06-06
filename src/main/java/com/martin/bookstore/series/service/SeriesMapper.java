package com.martin.bookstore.series.service;

import com.martin.bookstore.series.dto.SeriesRequestDto;
import com.martin.bookstore.series.dto.SeriesResponseDto;
import com.martin.bookstore.series.persistence.entity.Series;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface SeriesMapper {

    Series asEntity(SeriesRequestDto dto);

    SeriesResponseDto asOutput(Series entity);

    List<SeriesResponseDto> asOutput(List<Series> entities);

    void update(@MappingTarget Series entity, SeriesRequestDto dto);
}
