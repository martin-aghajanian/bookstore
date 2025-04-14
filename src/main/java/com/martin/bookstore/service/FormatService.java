package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.FormatRequestDto;
import com.martin.bookstore.dto.response.FormatResponseDto;
import com.martin.bookstore.entity.Format;
import com.martin.bookstore.core.mapper.FormatMapper;
import com.martin.bookstore.repository.FormatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatService {

    private final FormatRepository formatRepository;
    private final FormatMapper formatMapper;

    public FormatService(FormatRepository formatRepository, FormatMapper formatMapper) {
        this.formatRepository = formatRepository;
        this.formatMapper = formatMapper;
    }

    public List<FormatResponseDto> getAllFormats() {
        return formatMapper.asOutput(formatRepository.findAll());
    }

    public FormatResponseDto getFormatById(Long id) {
        return formatRepository.findById(id)
                .map(formatMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("format not found"));
    }

    public FormatResponseDto createFormat(FormatRequestDto dto) {
        Format saved = formatRepository.save(formatMapper.asEntity(dto));
        return formatMapper.asOutput(saved);
    }

    public FormatResponseDto updateFormat(Long id, FormatRequestDto dto) {
        Format format = formatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("format not found"));
        formatMapper.update(format, dto);
        return formatMapper.asOutput(formatRepository.save(format));
    }

    public void deleteFormat(Long id) {
        formatRepository.deleteById(id);
    }
}
