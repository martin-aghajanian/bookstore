package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "books_settings")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BookSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_settings_id_seq")
    @SequenceGenerator(name = "books_settings_id_seq", sequenceName = "books_settings_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;

}
