package com.martin.bookstore.service;

import com.martin.bookstore.core.exception.DeleteNotAllowedException;
import com.martin.bookstore.core.exception.NotFoundException;
import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.CharacterRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.CharacterResponseDto;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.Character;
import com.martin.bookstore.core.mapper.CharacterMapper;
import com.martin.bookstore.repository.BookCharacterRepository;
import com.martin.bookstore.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final CharacterMapper characterMapper;
    private final BookMapper bookMapper;

    public CharacterResponseDto getCharacterById(Long id) {
        return characterRepository.findById(id)
                .map(characterMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("character with id " + id + " not found"));
    }

    public CharacterResponseDto createCharacter(CharacterRequestDto dto) {
        Character saved = characterRepository.save(characterMapper.asEntity(dto));
        return characterMapper.asOutput(saved);
    }

    public CharacterResponseDto updateCharacter(Long id, CharacterRequestDto dto) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("character with id " + id + " not found"));
        characterMapper.update(character, dto);
        return characterMapper.asOutput(characterRepository.save(character));
    }

    public void deleteCharacter(Long id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("character with id " + id + " not found"));

        if (character.getBookCharacters() != null && !character.getBookCharacters().isEmpty()) {
            throw new DeleteNotAllowedException("cannot delete character associated with books");
        }

        characterRepository.delete(character);
    }

    public PageResponseDto<BookResponseDto> getBooksByCharacterId(Long characterId, int page, int size) {
        characterRepository.findById(characterId)
                .orElseThrow(() -> new NotFoundException("Character with id " + characterId + " not found"));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> books = bookCharacterRepository.findBooksByCharacterId(characterId, pageRequest);
        Page<BookResponseDto> dtoPage = books.map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }
}
