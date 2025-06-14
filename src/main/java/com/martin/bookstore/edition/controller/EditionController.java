package com.martin.bookstore.edition.controller;

import com.martin.bookstore.edition.dto.EditionRequestDto;
import com.martin.bookstore.edition.dto.EditionResponseDto;
import com.martin.bookstore.edition.service.EditionService;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/editions")
@RequiredArgsConstructor
public class EditionController {

    private final EditionService editionService;

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EditionResponseDto> getAllEditions() {
        return editionService.getAllEditions();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditionResponseDto getEditionById(@PathVariable Long id) {
        return editionService.getEditionById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditionResponseDto createEdition(@RequestBody EditionRequestDto dto) {
        return editionService.createEdition(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditionResponseDto updateEdition(@PathVariable Long id, @RequestBody EditionRequestDto dto) {
        return editionService.updateEdition(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEdition(@PathVariable Long id) {
        editionService.deleteEdition(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getBooksByEdition(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return editionService.getBooksByEdition(id, page, size);
    }
}
