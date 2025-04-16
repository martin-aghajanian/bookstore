package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.CharacterRequestDto;
import com.martin.bookstore.dto.response.CharacterResponseDto;
import com.martin.bookstore.entity.Character;
import com.martin.bookstore.core.mapper.CharacterMapper;
import com.martin.bookstore.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    public List<CharacterResponseDto> getAllCharacters() {
        return characterMapper.asOutput(characterRepository.findAll());
    }

    public CharacterResponseDto getCharacterById(Long id) {
        return characterRepository.findById(id)
                .map(characterMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("character not found"));
    }

    public CharacterResponseDto createCharacter(CharacterRequestDto dto) {
        Character saved = characterRepository.save(characterMapper.asEntity(dto));
        return characterMapper.asOutput(saved);
    }

    public CharacterResponseDto updateCharacter(Long id, CharacterRequestDto dto) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("character not found"));
        characterMapper.update(character, dto);
        return characterMapper.asOutput(characterRepository.save(character));
    }

    public void deleteCharacter(Long id) {
        characterRepository.deleteById(id);
    }
}
