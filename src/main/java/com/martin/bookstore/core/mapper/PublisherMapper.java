package com.martin.bookstore.core.mapper;

import com.martin.bookstore.entity.Publisher;
import com.martin.bookstore.dto.request.PublisherRequestDto;
import com.martin.bookstore.dto.response.PublisherResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface PublisherMapper {

    Publisher asEntity(PublisherRequestDto dto);

    PublisherResponseDto asOutput(Publisher entity);

    List<PublisherResponseDto> asOutput(List<Publisher> entities);

    void update(@MappingTarget Publisher entity, PublisherRequestDto dto);
}
