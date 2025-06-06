package com.martin.bookstore.wishlist.dto;

import com.martin.bookstore.book.dto.BookResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WishlistResponseDto {
    private Long id;
    private Long userId;
    private List<BookResponseDto> books;
}
