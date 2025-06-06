package com.martin.bookstore.wishlist.service;

import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.wishlist.dto.WishlistResponseDto;
import com.martin.bookstore.wishlist.persistence.entity.BookWishlist;
import com.martin.bookstore.wishlist.persistence.entity.Wishlist;
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
