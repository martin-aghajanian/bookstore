package com.martin.bookstore.book.persistence.entity;

import com.martin.bookstore.author.persistence.entity.Author;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "books_authors")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_authors_id_seq")
    @SequenceGenerator(name = "books_authors_id_seq", sequenceName = "books_authors_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "contribution", nullable = false)
    private String contribution;
}
