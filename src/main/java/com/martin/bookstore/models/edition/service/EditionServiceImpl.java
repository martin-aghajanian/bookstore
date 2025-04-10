package com.martin.bookstore.models.edition.service;

import com.martin.bookstore.models.edition.dto.EditionDto;
import com.martin.bookstore.models.edition.dto.EditionMapper;
import com.martin.bookstore.models.edition.persistence.entity.Edition;
import com.martin.bookstore.models.edition.persistence.repository.EditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EditionServiceImpl implements EditionService{

    private final EditionRepository editionRepository;
    private final EditionMapper editionMapper;

    public EditionServiceImpl(EditionRepository editionRepository, EditionMapper editionMapper) {
        this.editionRepository = editionRepository;
        this.editionMapper = editionMapper;
    }

    @Override
    public List<EditionDto> getAllEditions() {
        return editionRepository.findAll().stream().map(editionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public EditionDto getEditionById(Long id) {
        return editionRepository.findById(id).map(editionMapper::toDto).orElseThrow(() -> new RuntimeException("edition not found"));
    }

    @Override
    public EditionDto createEdition(EditionDto editionDto) {
        Edition saved = editionRepository.save(editionMapper.toEntity(editionDto));
        return editionMapper.toDto(saved);
    }

    @Override
    public EditionDto updateEdition(Long id, EditionDto updateEditionDto) {
        Optional<Edition> optionalEdition = editionRepository.findById(id);
        if (optionalEdition.isPresent()) {
            Edition edition = optionalEdition.get();
            edition.setName(updateEditionDto.getName());
            return editionMapper.toDto(editionRepository.save(edition));
        }
        return null;
    }

    @Override
    public void deleteEdition(Long id) {
        editionRepository.deleteById(id);
    }
}
