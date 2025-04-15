package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "books_awards")
@Getter
@Setter
@RequiredArgsConstructor
public class BookAward {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_awards_id_seq")
    @SequenceGenerator(name = "books_awards_id_seq", sequenceName = "books_awards_id_seq", allocationSize = 50)
    private Long id;

    // Many-to-one association to Book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Many-to-one association to Award
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "award_id", nullable = false)
    private Award award;

    @Column(name = "year", nullable = false)
    private LocalDate year;
}
