package com.martin.bookstore.models.format.dto;

import com.martin.bookstore.models.format.persistence.entity.Format;
import org.springframework.stereotype.Component;


@Component
public class FormatMapper {

    public FormatDto toDto(Format format) {
        FormatDto formatDto = new FormatDto();
        formatDto.setId(format.getId());
        formatDto.setFormat(format.getFormat());
        return formatDto;
    }

    public Format toEntity(FormatDto formatDto) {
        Format format = new Format();
        format.setId(formatDto.getId());
        format.setFormat(formatDto.getFormat());
        return format;
    }
}
