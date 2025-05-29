package com.martin.bookstore.wishlist;

import com.martin.bookstore.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books_wishlists")
@Getter
@Setter
@RequiredArgsConstructor
public class BookWishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_wishlists_id_seq")
    @SequenceGenerator(name = "books_wishlists_id_seq", sequenceName = "books_wishlists_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
