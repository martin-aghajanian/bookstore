package com.martin.bookstore.format.controller;

import com.martin.bookstore.format.dto.FormatRequestDto;
import com.martin.bookstore.format.dto.FormatResponseDto;
import com.martin.bookstore.format.service.FormatService;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/formats")
@RequiredArgsConstructor
public class FormatController {

    private final FormatService formatService;

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FormatResponseDto> getAllFormats() {
        return formatService.getAllFormats();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FormatResponseDto getFormatById(@PathVariable Long id) {
        return formatService.getFormatById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormatResponseDto createFormat(@RequestBody FormatRequestDto dto) {
        return formatService.createFormat(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FormatResponseDto updateFormat(@PathVariable Long id, @RequestBody FormatRequestDto dto) {
        return formatService.updateFormat(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFormat(@PathVariable Long id) {
        formatService.deleteFormat(id);
    }

    @PreAuthorize("permitAll()")
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