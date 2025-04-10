package com.martin.bookstore.models.format.service;

import com.martin.bookstore.models.format.dto.FormatDto;

import java.util.List;

public interface FormatService {

    List<FormatDto> getAllFormats();

    FormatDto getFormatById(Long id);

    FormatDto createFormat(FormatDto formatDto);

    FormatDto updateFormat(Long id, FormatDto updatedFormatdto);

    void deleteFormat(Long id);
}
