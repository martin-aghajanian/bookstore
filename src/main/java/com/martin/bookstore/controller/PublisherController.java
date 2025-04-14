package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.PublisherRequestDto;
import com.martin.bookstore.dto.response.PublisherResponseDto;
import com.martin.bookstore.service.PublisherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<PublisherResponseDto> getAllPublisherB() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/{id}")
    public PublisherResponseDto getPublisherById(@PathVariable Long id) {
        return publisherService.getPublisherById(id);
    }

    @PostMapping
    public PublisherResponseDto createPublisher(@RequestBody PublisherRequestDto dto) {
        return publisherService.createPublisher(dto);
    }

    @PutMapping("/{id}")
    public PublisherResponseDto updatePublisher(@PathVariable Long id, @RequestBody PublisherRequestDto dto) {
        return publisherService.updatePublisher(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }
}
