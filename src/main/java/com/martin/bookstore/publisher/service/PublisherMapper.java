package com.martin.bookstore.publisher.service;

import com.martin.bookstore.publisher.persistence.entity.Publisher;
import com.martin.bookstore.publisher.dto.PublisherRequestDto;
import com.martin.bookstore.publisher.dto.PublisherResponseDto;
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
