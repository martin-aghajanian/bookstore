package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.PublisherRequestDto;
import com.martin.bookstore.dto.response.PublisherResponseDto;
import com.martin.bookstore.entity.Publisher;
import com.martin.bookstore.core.mapper.PublisherMapper;
import com.martin.bookstore.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    public List<PublisherResponseDto> getAllPublishers() {
        return publisherMapper.asOutput(publisherRepository.findAll());
    }

    public PublisherResponseDto getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("publisher not found"));
    }

    public PublisherResponseDto createPublisher(PublisherRequestDto dto) {
        Publisher saved = publisherRepository.save(publisherMapper.asEntity(dto));
        return publisherMapper.asOutput(saved);
    }

    public PublisherResponseDto updatePublisher(Long id, PublisherRequestDto dto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("publisher not found"));
        publisherMapper.update(publisher, dto);
        return publisherMapper.asOutput(publisherRepository.save(publisher));
    }

    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }
}
