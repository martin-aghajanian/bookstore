package com.martin.bookstore.service;

import com.martin.bookstore.dto.old.PublisherDto;
import com.martin.bookstore.core.mapper.old.PublisherMapper;
import com.martin.bookstore.entity.Publisher;
import com.martin.bookstore.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    public List<PublisherDto> getAllPublishers() {
        return publisherRepository.findAll().stream().map(publisherMapper::toDto).collect(Collectors.toList());
    }

    public PublisherDto getPublisherById(Long id) {
        return publisherRepository.findById(id).map(publisherMapper::toDto).orElseThrow(() -> new RuntimeException("publisher not found"));
    }

    public PublisherDto createPublisher(PublisherDto publisherDto) {
        Publisher saved = publisherRepository.save(publisherMapper.toEntity(publisherDto));
        return publisherMapper.toDto(saved);
    }

    public PublisherDto updatePublisher(Long id, PublisherDto updatedPublisherDto) {
        Optional<Publisher> optionalPublisher = publisherRepository.findById(id);
        if (optionalPublisher.isPresent()) {
            Publisher publisher = optionalPublisher.get();
            publisher.setName(updatedPublisherDto.getName());
            return publisherMapper.toDto(publisherRepository.save(publisher));
        }
        return null;
    }

    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }
}
