package com.martin.bookstore.controller;

import com.martin.bookstore.dto.old.PublisherDto;
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
    public List<PublisherDto> getAllPublisherB() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/{id}")
    public PublisherDto getPublisherById(@PathVariable Long id) {
        return publisherService.getPublisherById(id);
    }

    @PostMapping
    public PublisherDto createPublisher(@RequestBody PublisherDto publisherDto) {
        return publisherService.createPublisher(publisherDto);
    }

    @PutMapping("/{id}")
    public PublisherDto updatePublisher(@PathVariable Long id, @RequestBody PublisherDto publisherDto) {
        return publisherService.updatePublisher(id, publisherDto);
    }

    @DeleteMapping("/{id}")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }
}
