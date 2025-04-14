package com.martin.bookstore.controller;

import com.martin.bookstore.dto.old.FormatDto;
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
    public List<FormatDto> getAllFormats() {
        return formatService.getAllFormats();
    }

    @GetMapping("/{id}")
    public FormatDto getFormatById(@PathVariable Long id) {
        return formatService.getFormatById(id);
    }

    @PostMapping
    public FormatDto createFormat(@RequestBody FormatDto formatDto) {
        return formatService.createFormat(formatDto);
    }

    @PutMapping("/{id}")
    public FormatDto updateFormat(@PathVariable Long id, @RequestBody FormatDto formatDto) {
        return formatService.updateFormat(id, formatDto);
    }

    @DeleteMapping("/{id}")
    public void deleteFormat(@PathVariable Long id) {
        formatService.deleteFormat(id);
    }
}
