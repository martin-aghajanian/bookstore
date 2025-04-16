package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.EditionRequestDto;
import com.martin.bookstore.dto.response.EditionResponseDto;
import com.martin.bookstore.entity.Edition;
import com.martin.bookstore.core.mapper.EditionMapper;
import com.martin.bookstore.repository.EditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditionService {

    private final EditionRepository editionRepository;
    private final EditionMapper editionMapper;

    public List<EditionResponseDto> getAllEditions() {
        return editionMapper.asOutput(editionRepository.findAll());
    }

    public EditionResponseDto getEditionById(Long id) {
        return editionRepository.findById(id)
                .map(editionMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("edition not found"));
    }

    public EditionResponseDto createEdition(EditionRequestDto dto) {
        Edition saved = editionRepository.save(editionMapper.asEntity(dto));
        return editionMapper.asOutput(saved);
    }

    public EditionResponseDto updateEdition(Long id, EditionRequestDto dto) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("edition not found"));
        editionMapper.update(edition, dto);
        return editionMapper.asOutput(editionRepository.save(edition));
    }

    public void deleteEdition(Long id) {
        editionRepository.deleteById(id);
    }
}
