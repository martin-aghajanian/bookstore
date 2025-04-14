package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.CharacterRequestDto;
import com.martin.bookstore.dto.response.CharacterResponseDto;
import com.martin.bookstore.service.CharacterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    public List<CharacterResponseDto> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @GetMapping("/{id}")
    public CharacterResponseDto getCharacterById(@PathVariable Long id) {
        return characterService.getCharacterById(id);
    }

    @PostMapping
    public CharacterResponseDto createCharacter(@RequestBody CharacterRequestDto dto) {
        return characterService.createCharacter(dto);
    }

    @PutMapping("/{id}")
    public CharacterResponseDto updateCharacter(@PathVariable Long id, @RequestBody CharacterRequestDto dto) {
        return characterService.updateCharacter(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
    }
}
