package com.martin.bookstore.service;

import com.martin.bookstore.dto.EditionDto;
import com.martin.bookstore.core.mapper.EditionMapper;
import com.martin.bookstore.entity.Edition;
import com.martin.bookstore.repository.EditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EditionService {

    private final EditionRepository editionRepository;
    private final EditionMapper editionMapper;

    public EditionService(EditionRepository editionRepository, EditionMapper editionMapper) {
        this.editionRepository = editionRepository;
        this.editionMapper = editionMapper;
    }

    public List<EditionDto> getAllEditions() {
        return editionRepository.findAll().stream().map(editionMapper::toDto).collect(Collectors.toList());
    }

    public EditionDto getEditionById(Long id) {
        return editionRepository.findById(id).map(editionMapper::toDto).orElseThrow(() -> new RuntimeException("edition not found"));
    }

    public EditionDto createEdition(EditionDto editionDto) {
        Edition saved = editionRepository.save(editionMapper.toEntity(editionDto));
        return editionMapper.toDto(saved);
    }

    public EditionDto updateEdition(Long id, EditionDto updateEditionDto) {
        Optional<Edition> optionalEdition = editionRepository.findById(id);
        if (optionalEdition.isPresent()) {
            Edition edition = optionalEdition.get();
            edition.setName(updateEditionDto.getName());
            return editionMapper.toDto(editionRepository.save(edition));
        }
        return null;
    }

    public void deleteEdition(Long id) {
        editionRepository.deleteById(id);
    }
}
