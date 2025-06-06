package com.martin.bookstore.character.service;

import com.martin.bookstore.character.persistence.repository.CharacterRepository;
import com.martin.bookstore.character.dto.CharacterRequestDto;
import com.martin.bookstore.character.dto.CharacterResponseDto;
import com.martin.bookstore.character.persistence.entity.Character;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.repository.BookCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        com.martin.bookstore.character.persistence.entity.Character character = characterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("character with id " + id + " not found"));
        characterMapper.update(character, dto);
        return characterMapper.asOutput(characterRepository.save(character));
    }

    public void deleteCharacter(Long id) {
        com.martin.bookstore.character.persistence.entity.Character character = characterRepository.findById(id)
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
