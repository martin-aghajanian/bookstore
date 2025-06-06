package com.martin.bookstore.format.service;

import com.martin.bookstore.format.dto.FormatRequestDto;
import com.martin.bookstore.format.dto.FormatResponseDto;
import com.martin.bookstore.format.persistence.entity.Format;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface FormatMapper {

    Format asEntity(FormatRequestDto dto);

    FormatResponseDto asOutput(Format entity);

    List<FormatResponseDto> asOutput(List<Format> entities);

    void update(@MappingTarget Format entity, FormatRequestDto dto);
}
