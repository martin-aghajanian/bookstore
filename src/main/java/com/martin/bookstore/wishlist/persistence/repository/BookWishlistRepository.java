package com.martin.bookstore.wishlist.persistence.repository;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.wishlist.persistence.entity.BookWishlist;
import com.martin.bookstore.wishlist.persistence.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookWishlistRepository extends JpaRepository<BookWishlist, Long> {

    List<BookWishlist> findAllByWishlist(Wishlist wishlist);

    Optional<BookWishlist> findByWishlistAndBook(Wishlist wishlist, Book book);

    void deleteByWishlistAndBook(Wishlist wishlist, Book book);
}
