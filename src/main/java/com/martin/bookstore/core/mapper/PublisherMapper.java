package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.PublisherDto;
import com.martin.bookstore.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public PublisherDto toDto(Publisher publisher) {
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisher.getId());
        publisherDto.setName(publisher.getName());
        return publisherDto;
    }

    public Publisher toEntity(PublisherDto publisherDto) {
        Publisher publisher = new Publisher();
        publisher.setId(publisherDto.getId());
        publisher.setName(publisherDto.getName());
        return publisher;
    }

}
