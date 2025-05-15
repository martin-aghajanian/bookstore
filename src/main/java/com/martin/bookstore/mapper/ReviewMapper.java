package com.martin.bookstore.mapper;

import com.martin.bookstore.dto.request.ReviewRequestDto;
import com.martin.bookstore.dto.response.ReviewResponseDto;
import com.martin.bookstore.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review asEntity(ReviewRequestDto dto);

    @Mapping(target = "id", source = "id")
    ReviewResponseDto asDto(Review review);
}
