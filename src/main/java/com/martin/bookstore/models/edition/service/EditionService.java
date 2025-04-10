package com.martin.bookstore.models.edition.service;

import com.martin.bookstore.models.edition.dto.EditionDto;

import java.util.List;

public interface EditionService {

    List<EditionDto> getAllEditions();

    EditionDto getEditionById(Long id);

    EditionDto createEdition(EditionDto editionDto);

    EditionDto updateEdition(Long id, EditionDto updateEditionDto);

    void deleteEdition(Long id);

}
