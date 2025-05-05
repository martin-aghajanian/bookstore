package com.martin.bookstore.controller;

import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.CharacterRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.CharacterResponseDto;
import com.martin.bookstore.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDto getCharacterById(@PathVariable Long id) {
        return characterService.getCharacterById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterResponseDto createCharacter(@RequestBody CharacterRequestDto dto) {
        return characterService.createCharacter(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDto updateCharacter(@PathVariable Long id, @RequestBody CharacterRequestDto dto) {
        return characterService.updateCharacter(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getBooksByCharacter(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return characterService.getBooksByCharacterId(id, page, size);
    }
}
