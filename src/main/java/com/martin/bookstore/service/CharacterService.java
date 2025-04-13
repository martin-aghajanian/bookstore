package com.martin.bookstore.service;

import com.martin.bookstore.dto.CharacterDto;
import com.martin.bookstore.core.mapper.CharacterMapper;
import com.martin.bookstore.entity.Character;
import com.martin.bookstore.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    public CharacterService(CharacterRepository characterRepository, CharacterMapper characterMapper) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
    }

    public List<CharacterDto> getAllCharacters() {
        return characterRepository.findAll().stream().map(characterMapper::toDto).collect(Collectors.toList());
    }

    public CharacterDto getCharacterById(Long id) {
        return characterRepository.findById(id).map(characterMapper::toDto).orElseThrow(() -> new RuntimeException("character not found"));
    }

    public CharacterDto createCharacter(CharacterDto characterDto) {
        Character saved = characterRepository.save(characterMapper.toEntity(characterDto));
        return characterMapper.toDto(saved);
    }

    public CharacterDto updateCharacter(Long id, CharacterDto updatedCharacterDto) {
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        if (optionalCharacter.isPresent()) {
            Character character = optionalCharacter.get();
            character.setName(updatedCharacterDto.getName());
            return characterMapper.toDto(characterRepository.save(character));
        }
        return null;
    }

    public void deleteCharacter(Long id) {
        characterRepository.deleteById(id);
    }
}
