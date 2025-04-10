package com.martin.bookstore.models.character.service;

import com.martin.bookstore.models.character.dto.CharacterDto;
import com.martin.bookstore.models.character.dto.CharacterMapper;
import com.martin.bookstore.models.character.persistence.entity.Character;
import com.martin.bookstore.models.character.persistence.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CharacterServiceImpl implements CharacterService{

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    public CharacterServiceImpl(CharacterRepository characterRepository, CharacterMapper characterMapper) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
    }

    @Override
    public List<CharacterDto> getAllCharacters() {
        return characterRepository.findAll().stream().map(characterMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CharacterDto getCharacterById(Long id) {
        return characterRepository.findById(id).map(characterMapper::toDto).orElseThrow(() -> new RuntimeException("character not found"));
    }

    @Override
    public CharacterDto createCharacter(CharacterDto characterDto) {
        Character saved = characterRepository.save(characterMapper.toEntity(characterDto));
        return characterMapper.toDto(saved);
    }

    @Override
    public CharacterDto updateCharacter(Long id, CharacterDto updatedCharacterDto) {
        Optional<Character> optionalCharacter = characterRepository.findById(id);
        if (optionalCharacter.isPresent()) {
            Character character = optionalCharacter.get();
            character.setName(updatedCharacterDto.getName());
            return characterMapper.toDto(characterRepository.save(character));
        }
        return null;
    }

    @Override
    public void deleteCharacter(Long id) {
        characterRepository.deleteById(id);
    }
}
