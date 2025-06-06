package com.martin.bookstore.review.service;

import com.martin.bookstore.review.dto.ReviewRequestDto;
import com.martin.bookstore.review.dto.ReviewResponseDto;
import com.martin.bookstore.review.persistence.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review asEntity(ReviewRequestDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "user.username")
    ReviewResponseDto asDto(Review review);
}
