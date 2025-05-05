package com.martin.bookstore.controller;

import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.EditionRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.EditionResponseDto;
import com.martin.bookstore.service.EditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/editions")
@RequiredArgsConstructor
public class EditionController {

    private final EditionService editionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EditionResponseDto> getAllEditions() {
        return editionService.getAllEditions();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditionResponseDto getEditionById(@PathVariable Long id) {
        return editionService.getEditionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditionResponseDto createEdition(@RequestBody EditionRequestDto dto) {
        return editionService.createEdition(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditionResponseDto updateEdition(@PathVariable Long id, @RequestBody EditionRequestDto dto) {
        return editionService.updateEdition(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEdition(@PathVariable Long id) {
        editionService.deleteEdition(id);
    }

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
