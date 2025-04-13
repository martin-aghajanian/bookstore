package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.CharacterDto;
import com.martin.bookstore.entity.Character;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

    public CharacterDto toDto(Character character) {
        CharacterDto characterDto = new CharacterDto();
        characterDto.setId(character.getId());
        characterDto.setName(character.getName());
        return characterDto;
    }

    public Character toEntity(CharacterDto characterDto) {
        Character character = new Character();
        character.setId(characterDto.getId());
        character.setName(characterDto.getName());
        return character;
    }
}
