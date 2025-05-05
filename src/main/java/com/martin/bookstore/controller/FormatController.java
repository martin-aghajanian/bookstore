package com.martin.bookstore.controller;

import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.FormatRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.FormatResponseDto;
import com.martin.bookstore.service.FormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/formats")
@RequiredArgsConstructor
public class FormatController {

    private final FormatService formatService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FormatResponseDto> getAllFormats() {
        return formatService.getAllFormats();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FormatResponseDto getFormatById(@PathVariable Long id) {
        return formatService.getFormatById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormatResponseDto createFormat(@RequestBody FormatRequestDto dto) {
        return formatService.createFormat(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FormatResponseDto updateFormat(@PathVariable Long id, @RequestBody FormatRequestDto dto) {
        return formatService.updateFormat(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFormat(@PathVariable Long id) {
        formatService.deleteFormat(id);
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getBooksByFormat(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return formatService.getBooksByFormat(id, page, size);
    }
}