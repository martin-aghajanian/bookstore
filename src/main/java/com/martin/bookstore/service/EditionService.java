package com.martin.bookstore.service;

import com.martin.bookstore.core.exception.DeleteNotAllowedException;
import com.martin.bookstore.core.exception.NotFoundException;
import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.EditionRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.EditionResponseDto;
import com.martin.bookstore.entity.Edition;
import com.martin.bookstore.core.mapper.EditionMapper;
import com.martin.bookstore.repository.BookRepository;
import com.martin.bookstore.repository.EditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
