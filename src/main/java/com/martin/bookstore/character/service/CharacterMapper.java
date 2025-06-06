package com.martin.bookstore.character.service;

import com.martin.bookstore.character.dto.CharacterRequestDto;
import com.martin.bookstore.character.dto.CharacterResponseDto;
import com.martin.bookstore.character.persistence.entity.Character;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CharacterMapper {

    com.martin.bookstore.character.persistence.entity.Character asEntity(CharacterRequestDto dto);

    CharacterResponseDto asOutput(Character entity);

    List<CharacterResponseDto> asOutput(List<com.martin.bookstore.character.persistence.entity.Character> entities);

    void update(@MappingTarget com.martin.bookstore.character.persistence.entity.Character entity, CharacterRequestDto dto);
}

