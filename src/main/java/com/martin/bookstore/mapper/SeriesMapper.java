package com.martin.bookstore.mapper;

import com.martin.bookstore.entity.Series;
import com.martin.bookstore.dto.request.SeriesRequestDto;
import com.martin.bookstore.dto.response.SeriesResponseDto;
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
