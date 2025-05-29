package com.martin.bookstore.wishlist;

import com.martin.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookWishlistRepository extends JpaRepository<BookWishlist, Long> {

    List<BookWishlist> findAllByWishlist(Wishlist wishlist);

    Optional<BookWishlist> findByWishlistAndBook(Wishlist wishlist, Book book);

    void deleteByWishlistAndBook(Wishlist wishlist, Book book);
}
