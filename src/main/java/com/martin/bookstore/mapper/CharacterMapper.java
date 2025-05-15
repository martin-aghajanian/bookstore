package com.martin.bookstore.mapper;

import com.martin.bookstore.entity.Character;
import com.martin.bookstore.dto.request.CharacterRequestDto;
import com.martin.bookstore.dto.response.CharacterResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CharacterMapper {

    Character asEntity(CharacterRequestDto dto);

    CharacterResponseDto asOutput(Character entity);

    List<CharacterResponseDto> asOutput(List<Character> entities);

    void update(@MappingTarget Character entity, CharacterRequestDto dto);
}

