package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.FormatRequestDto;
import com.martin.bookstore.dto.response.FormatResponseDto;
import com.martin.bookstore.service.FormatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/formats")
public class FormatController {

    private final FormatService formatService;

    public FormatController(FormatService formatService) {
        this.formatService = formatService;
    }

    @GetMapping
    public List<FormatResponseDto> getAllFormats() {
        return formatService.getAllFormats();
    }

    @GetMapping("/{id}")
    public FormatResponseDto getFormatById(@PathVariable Long id) {
        return formatService.getFormatById(id);
    }

    @PostMapping
    public FormatResponseDto createFormat(@RequestBody FormatRequestDto dto) {
        return formatService.createFormat(dto);
    }

    @PutMapping("/{id}")
    public FormatResponseDto updateFormat(@PathVariable Long id, @RequestBody FormatRequestDto dto) {
        return formatService.updateFormat(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteFormat(@PathVariable Long id) {
        formatService.deleteFormat(id);
    }
}