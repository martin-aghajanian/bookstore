package com.martin.bookstore.models.edition.controller;

import com.martin.bookstore.models.edition.dto.EditionDto;
import com.martin.bookstore.models.edition.service.EditionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/Editions")
public class EditionController {

    private final EditionService editionService;

    public EditionController(EditionService editionService) {
        this.editionService = editionService;
    }

    @GetMapping
    public List<EditionDto> getAllEditions() {
        return editionService.getAllEditions();
    }

    @GetMapping("/{id}")
    public EditionDto getEditionById(@PathVariable Long id) {
        return editionService.getEditionById(id);
    }

    @PostMapping
    public EditionDto createEdition(@RequestBody EditionDto editionDto) {
        return editionService.createEdition(editionDto);
    }

    @PutMapping("/{id}")
    public EditionDto updateEdition(@PathVariable Long id, @RequestBody EditionDto editionDto) {
        return editionService.updateEdition(id, editionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEdition(@PathVariable Long id) {
        editionService.deleteEdition(id);
    }
}
