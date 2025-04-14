package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.EditionRequestDto;
import com.martin.bookstore.dto.response.EditionResponseDto;
import com.martin.bookstore.service.EditionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/editions")
public class EditionController {

    private final EditionService editionService;

    public EditionController(EditionService editionService) {
        this.editionService = editionService;
    }

    @GetMapping
    public List<EditionResponseDto> getAllEditions() {
        return editionService.getAllEditions();
    }

    @GetMapping("/{id}")
    public EditionResponseDto getEditionById(@PathVariable Long id) {
        return editionService.getEditionById(id);
    }

    @PostMapping
    public EditionResponseDto createEdition(@RequestBody EditionRequestDto dto) {
        return editionService.createEdition(dto);
    }

    @PutMapping("/{id}")
    public EditionResponseDto updateEdition(@PathVariable Long id, @RequestBody EditionRequestDto dto) {
        return editionService.updateEdition(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEdition(@PathVariable Long id) {
        editionService.deleteEdition(id);
    }
}
