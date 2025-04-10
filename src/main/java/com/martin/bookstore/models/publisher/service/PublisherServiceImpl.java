package com.martin.bookstore.models.publisher.service;

import com.martin.bookstore.models.publisher.dto.PublisherDto;
import com.martin.bookstore.models.publisher.dto.PublisherMapper;
import com.martin.bookstore.models.publisher.persistence.entity.Publisher;
import com.martin.bookstore.models.publisher.persistence.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService{

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    @Override
    public List<PublisherDto> getAllPublishers() {
        return publisherRepository.findAll().stream().map(publisherMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PublisherDto getPublisherById(Long id) {
        return publisherRepository.findById(id).map(publisherMapper::toDto).orElseThrow(() -> new RuntimeException("publisher not found"));
    }

    @Override
    public PublisherDto createPublisher(PublisherDto publisherDto) {
        Publisher saved = publisherRepository.save(publisherMapper.toEntity(publisherDto));
        return publisherMapper.toDto(saved);
    }

    @Override
    public PublisherDto updatePublisher(Long id, PublisherDto updatedPublisherDto) {
        Optional<Publisher> optionalPublisher = publisherRepository.findById(id);
        if (optionalPublisher.isPresent()) {
            Publisher publisher = optionalPublisher.get();
            publisher.setName(updatedPublisherDto.getName());
            return publisherMapper.toDto(publisherRepository.save(publisher));
        }
        return null;
    }

    @Override
    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }
}
