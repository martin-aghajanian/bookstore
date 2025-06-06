package com.martin.bookstore.wishlist.service;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.user.persistence.entity.User;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.shared.exception.WishlistNotFoundException;
import com.martin.bookstore.book.persistence.repository.BookRepository;
import com.martin.bookstore.security.config.CustomUserDetails;
import com.martin.bookstore.wishlist.dto.WishlistResponseDto;
import com.martin.bookstore.wishlist.persistence.entity.BookWishlist;
import com.martin.bookstore.wishlist.persistence.entity.Wishlist;
import com.martin.bookstore.wishlist.persistence.repository.BookWishlistRepository;
import com.martin.bookstore.wishlist.persistence.repository.WishlistRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final BookWishlistRepository bookWishlistRepository;
    private final BookRepository bookRepository;
    private final WishlistMapper wishlistMapper;

    @Transactional
    public WishlistResponseDto addBookToWishlist(Long bookId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        boolean alreadyExists = bookWishlistRepository.findByWishlistAndBook(wishlist, book).isPresent();
        if (!alreadyExists) {
            BookWishlist entry = new BookWishlist();
            entry.setWishlist(wishlist);
            entry.setBook(book);
            bookWishlistRepository.save(entry);
        }

        List<BookWishlist> wishlistBooks = bookWishlistRepository.findAllByWishlist(wishlist);
        return wishlistMapper.asOutput(wishlist, wishlistBooks);
    }

    @Transactional(readOnly = true)
    public WishlistResponseDto getAuthenticatedUserWishlist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new WishlistNotFoundException(user.getId()));

        List<BookWishlist> wishlistBooks = bookWishlistRepository.findAllByWishlist(wishlist);
        return wishlistMapper.asOutput(wishlist, wishlistBooks);
    }

    @Transactional
    public void removeBookFromWishlist(Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(WishlistNotFoundException::new);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + bookId));

        bookWishlistRepository.findByWishlistAndBook(wishlist, book)
                .ifPresent(bookWishlistRepository::delete);
    }
}
