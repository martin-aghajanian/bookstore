package com.martin.bookstore.models.edition.dto;

import com.martin.bookstore.models.edition.persistence.entity.Edition;
import org.springframework.stereotype.Component;

@Component
public class EditionMapper {

    public EditionDto toDto(Edition edition) {
        EditionDto editionDto = new EditionDto();
        editionDto.setId(edition.getId());
        editionDto.setName(edition.getName());
        return editionDto;
    }

    public Edition toEntity(EditionDto editionDto) {
        Edition edition = new Edition();
        edition.setId(editionDto.getId());
        edition.setName(editionDto.getName());
        return edition;
    }

}
