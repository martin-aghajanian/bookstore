package com.martin.bookstore.wishlist;

import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WishlistMapper {

    private final BookMapper bookMapper;

    public WishlistResponseDto asOutput(Wishlist wishlist, List<BookWishlist> bookWishlistList) {
        List<BookResponseDto> books = bookWishlistList.stream()
                .map(BookWishlist::getBook)
                .map(bookMapper::asOutput)
                .toList();

        return WishlistResponseDto.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .books(books)
                .build();
    }
}
