package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "books_characters")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BookCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_characters_id_seq")
    @SequenceGenerator(name = "books_characters_id_seq", sequenceName = "books_characters_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;


}
