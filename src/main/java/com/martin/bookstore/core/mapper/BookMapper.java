package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.request.BookRequestDto;
import com.martin.bookstore.dto.response.*;
import com.martin.bookstore.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Autowired protected AuthorMapper authorMapper;
    @Autowired protected GenreMapper genreMapper;
    @Autowired protected CharacterMapper characterMapper;
    @Autowired protected SettingMapper settingMapper;
    @Autowired protected AwardMapper awardMapper;

    public abstract Book asEntity(BookRequestDto dto);

    @Mapping(target = "authors", expression = "java(mapAuthors(entity.getBookAuthor()))")
    @Mapping(target = "genres", expression = "java(mapGenres(entity.getBookGenres()))")
    @Mapping(target = "characters", expression = "java(mapCharacters(entity.getBookCharacters()))")
    @Mapping(target = "settings", expression = "java(mapSettings(entity.getBookSetting()))")
    @Mapping(target = "awards", expression = "java(mapAwards(entity.getBookAwards()))")
    public abstract BookResponseDto asOutput(Book entity);

    public abstract List<BookResponseDto> asOutput(List<Book> entities);

    public abstract void update(@MappingTarget Book entity, BookRequestDto dto);


    protected List<AuthorResponseDto> mapAuthors(List<BookAuthor> links) {
        return links == null ? null : links.stream()
                .map(link -> authorMapper.asOutput(link.getAuthor()))
                .collect(Collectors.toList());
    }

    protected List<GenreResponseDto> mapGenres(List<BookGenre> links) {
        return links == null ? null : links.stream()
                .map(link -> genreMapper.asOutput(link.getGenre()))
                .collect(Collectors.toList());
    }

    protected List<CharacterResponseDto> mapCharacters(List<BookCharacter> links) {
        return links == null ? null : links.stream()
                .map(link -> characterMapper.asOutput(link.getCharacter()))
                .collect(Collectors.toList());
    }

    protected List<SettingResponseDto> mapSettings(List<BookSetting> links) {
        return links == null ? null : links.stream()
                .map(link -> settingMapper.asOutput(link.getSetting()))
                .collect(Collectors.toList());
    }

    protected List<AwardResponseDto> mapAwards(List<BookAward> links) {
        return links == null ? null : links.stream()
                .map(link -> awardMapper.asOutput(link.getAward()))
                .collect(Collectors.toList());
    }
}
