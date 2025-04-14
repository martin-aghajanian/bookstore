package com.martin.bookstore.service;

import com.martin.bookstore.dto.old.FormatDto;
import com.martin.bookstore.core.mapper.old.FormatMapper;
import com.martin.bookstore.entity.Format;
import com.martin.bookstore.repository.FormatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormatService {

    private final FormatRepository formatRepository;
    private final FormatMapper formatMapper;

    public FormatService(FormatRepository formatRepository, FormatMapper formatMapper) {
        this.formatRepository = formatRepository;
        this.formatMapper = formatMapper;
    }

    public List<FormatDto> getAllFormats() {
        return formatRepository.findAll().stream().map(formatMapper::toDto).collect(Collectors.toList());
    }

    public FormatDto getFormatById(Long id) {
        return formatRepository.findById(id).map(formatMapper::toDto)
                .orElseThrow(() -> new RuntimeException("format not found"));
    }

    public FormatDto createFormat(FormatDto formatDto) {
        Format saved = formatRepository.save(formatMapper.toEntity(formatDto));
        return formatMapper.toDto(saved);
    }

    public FormatDto updateFormat(Long id, FormatDto updatedFormatdto) {
        Optional<Format> optionalFormat = formatRepository.findById(id);
        if (optionalFormat.isPresent()) {
            Format format = optionalFormat.get();
            format.setFormat(updatedFormatdto.getFormat());
            return formatMapper.toDto(formatRepository.save(format));
        }
        return null;
    }

    public void deleteFormat(Long id) {
        formatRepository.deleteById(id);
    }
}
