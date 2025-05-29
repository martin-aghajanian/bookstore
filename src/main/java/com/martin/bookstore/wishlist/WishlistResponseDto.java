package com.martin.bookstore.wishlist;

import com.martin.bookstore.dto.response.BookResponseDto;
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
