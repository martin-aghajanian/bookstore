package com.martin.bookstore.edition.service;

import com.martin.bookstore.edition.dto.EditionRequestDto;
import com.martin.bookstore.edition.dto.EditionResponseDto;
import com.martin.bookstore.edition.persistence.entity.Edition;
import com.martin.bookstore.edition.persistence.repository.EditionRepository;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditionService {

    private final EditionRepository editionRepository;
    private final EditionMapper editionMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<EditionResponseDto> getAllEditions() {
        return editionMapper.asOutput(editionRepository.findAll());
    }

    public EditionResponseDto getEditionById(Long id) {
        return editionRepository.findById(id)
                .map(editionMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("edition with id " + id + " not found"));
    }

    public EditionResponseDto createEdition(EditionRequestDto dto) {
        Edition saved = editionRepository.save(editionMapper.asEntity(dto));
        return editionMapper.asOutput(saved);
    }

    public EditionResponseDto updateEdition(Long id, EditionRequestDto dto) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("edition with id " + id + " not found"));
        editionMapper.update(edition, dto);
        return editionMapper.asOutput(editionRepository.save(edition));
    }

    public void deleteEdition(Long id) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("edition with id " + id + " not found"));

        if (edition.getBooks() != null && !edition.getBooks().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete edition associated with books");
        }

        editionRepository.delete(edition);
    }

    public PageResponseDto<BookResponseDto> getBooksByEdition(Long editionId, int page, int size) {
        editionRepository.findById(editionId)
                .orElseThrow(() -> new NotFoundException("edition with id " + editionId + " not found"));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BookResponseDto> dtoPage = bookRepository
                .findByEditionId(editionId, pageRequest)
                .map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }
}
