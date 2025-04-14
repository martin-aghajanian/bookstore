package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.request.BookRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = {
        EditionMapper.class,
        SeriesMapper.class,
        LanguageMapper.class,
        PublisherMapper.class,
        FormatMapper.class,
        AuthorMapper.class,
        GenreMapper.class,
        CharacterMapper.class,
        SettingMapper.class,
        AwardMapper.class
})
public interface BookMapper {

    Book asEntity(BookRequestDto dto);

    BookResponseDto asOutput(Book entity);

    List<BookResponseDto> asOutput(List<Book> entities);

    void update(@MappingTarget Book entity, BookRequestDto dto);
}
