package com.martin.bookstore.controller;

import com.martin.bookstore.dto.CharacterDto;
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
    public List<CharacterDto> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @GetMapping("/{id}")
    public CharacterDto getCharacterById(@PathVariable Long id) {
        return characterService.getCharacterById(id);
    }

    @PostMapping
    public CharacterDto createCharacter(@RequestBody CharacterDto characterDto) {
        return characterService.createCharacter(characterDto);
    }

    @PutMapping("/{id}")
    public CharacterDto updateCharacter(@PathVariable Long id, @RequestBody CharacterDto characterDto) {
        return characterService.updateCharacter(id, characterDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
    }
}
