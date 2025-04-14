package com.martin.bookstore.core.mapper;

import com.martin.bookstore.entity.Format;
import com.martin.bookstore.dto.request.FormatRequestDto;
import com.martin.bookstore.dto.response.FormatResponseDto;
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
