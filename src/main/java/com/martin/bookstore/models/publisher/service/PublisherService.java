package com.martin.bookstore.models.publisher.service;


import com.martin.bookstore.models.publisher.dto.PublisherDto;

import java.util.List;

public interface PublisherService {

    List<PublisherDto> getAllPublishers();

    PublisherDto getPublisherById(Long id);

    PublisherDto createPublisher(PublisherDto publisherDto);

    PublisherDto updatePublisher(Long id, PublisherDto updatedPublisherDto);

    void deletePublisher(Long id);

}
