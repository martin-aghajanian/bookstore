package com.martin.bookstore.models.format.service;

import com.martin.bookstore.models.format.dto.FormatDto;
import com.martin.bookstore.models.format.dto.FormatMapper;
import com.martin.bookstore.models.format.persistence.entity.Format;
import com.martin.bookstore.models.format.persistence.repository.FormatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormatServiceImpl implements FormatService{

    private final FormatRepository formatRepository;
    private final FormatMapper formatMapper;

    public FormatServiceImpl(FormatRepository formatRepository, FormatMapper formatMapper) {
        this.formatRepository = formatRepository;
        this.formatMapper = formatMapper;
    }

    @Override
    public List<FormatDto> getAllFormats() {
        return formatRepository.findAll().stream().map(formatMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public FormatDto getFormatById(Long id) {
        return formatRepository.findById(id).map(formatMapper::toDto)
                .orElseThrow(() -> new RuntimeException("format not found"));
    }

    @Override
    public FormatDto createFormat(FormatDto formatDto) {
        Format saved = formatRepository.save(formatMapper.toEntity(formatDto));
        return formatMapper.toDto(saved);
    }

    @Override
    public FormatDto updateFormat(Long id, FormatDto updatedFormatdto) {
        Optional<Format> optionalFormat = formatRepository.findById(id);
        if (optionalFormat.isPresent()) {
            Format format = optionalFormat.get();
            format.setFormat(updatedFormatdto.getFormat());
            return formatMapper.toDto(formatRepository.save(format));
        }
        return null;
    }

    @Override
    public void deleteFormat(Long id) {
        formatRepository.deleteById(id);
    }
}
