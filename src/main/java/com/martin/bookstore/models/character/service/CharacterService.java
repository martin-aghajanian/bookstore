package com.martin.bookstore.models.character.service;

import com.martin.bookstore.models.character.dto.CharacterDto;

import java.util.List;

public interface CharacterService {

    List<CharacterDto> getAllCharacters();

    CharacterDto getCharacterById(Long id);

    CharacterDto createCharacter(CharacterDto characterDto);

    CharacterDto updateCharacter(Long id, CharacterDto updatedCharacterDto);

    void deleteCharacter(Long id);

}
