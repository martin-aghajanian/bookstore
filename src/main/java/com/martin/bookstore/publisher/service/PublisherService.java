package com.martin.bookstore.publisher.service;

import com.martin.bookstore.publisher.persistence.entity.Publisher;
import com.martin.bookstore.publisher.persistence.repository.PublisherRepository;
import com.martin.bookstore.publisher.dto.PublisherRequestDto;
import com.martin.bookstore.publisher.dto.PublisherResponseDto;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final PublisherMapper publisherMapper;
    private final BookMapper bookMapper;

    public Page<PublisherResponseDto> getAllPublishers(Pageable pageable) {
        return publisherRepository.findAll(pageable).map(publisherMapper::asOutput);
    }

    public PublisherResponseDto getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("publisher with id " + id + " not found"));
    }

    public PublisherResponseDto createPublisher(PublisherRequestDto dto) {
        Publisher saved = publisherRepository.save(publisherMapper.asEntity(dto));
        return publisherMapper.asOutput(saved);
    }

    public PublisherResponseDto updatePublisher(Long id, PublisherRequestDto dto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("publisher with id " + id + " not found"));
        publisherMapper.update(publisher, dto);
        return publisherMapper.asOutput(publisherRepository.save(publisher));
    }

    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher with id " + id + " not found"));

        if (!publisher.getBooks().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete publisher: it is associated with books.");
        }

        publisherRepository.delete(publisher);
    }

    public Page<PublisherResponseDto> searchByName(String name, Pageable pageable) {
        return publisherRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(publisherMapper::asOutput);
    }

    public PageResponseDto<BookResponseDto> getBooksByPublisherId(Long publisherId, int page, int size) {
        publisherRepository.findById(publisherId)
                .orElseThrow(() -> new NotFoundException("publisher with id " + publisherId + " not found"));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByPublisherId(publisherId, pageRequest);
        Page<BookResponseDto> dtoPage = bookPage.map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }

}
