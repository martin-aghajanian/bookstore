package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.PublisherRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.PublisherResponseDto;
import com.martin.bookstore.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherResponseDto getPublisherById(@PathVariable Long id) {
        return publisherService.getPublisherById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherResponseDto createPublisher(@RequestBody PublisherRequestDto dto) {
        return publisherService.createPublisher(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PublisherResponseDto updatePublisher(@PathVariable Long id, @RequestBody PublisherRequestDto dto) {
        return publisherService.updatePublisher(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<PublisherResponseDto> searchPublishers(@RequestParam String name,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return publisherService.searchByName(name, PageRequest.of(page, size));
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookResponseDto> getBooksByPublisher(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return publisherService.getBooksByPublisherId(id, PageRequest.of(page, size));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PublisherResponseDto> getAllPublishers(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return publisherService.getAllPublishers(PageRequest.of(page, size));
    }
}
