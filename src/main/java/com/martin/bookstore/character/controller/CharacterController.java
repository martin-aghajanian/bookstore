package com.martin.bookstore.character.controller;

import com.martin.bookstore.character.dto.CharacterRequestDto;
import com.martin.bookstore.character.dto.CharacterResponseDto;
import com.martin.bookstore.character.service.CharacterService;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDto getCharacterById(@PathVariable Long id) {
        return characterService.getCharacterById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterResponseDto createCharacter(@RequestBody CharacterRequestDto dto) {
        return characterService.createCharacter(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDto updateCharacter(@PathVariable Long id, @RequestBody CharacterRequestDto dto) {
        return characterService.updateCharacter(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
    }

    @PreAuthorize("permitAll()")
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
