package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Table(name = "books_genres")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BookGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_genres_id_seq")
    @SequenceGenerator(name = "books_genres_id_seq", sequenceName = "books_genres_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
