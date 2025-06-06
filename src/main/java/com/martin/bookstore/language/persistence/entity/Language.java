package com.martin.bookstore.language.persistence.entity;

import com.martin.bookstore.book.persistence.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "languages")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "languages_id_seq")
    @SequenceGenerator(name = "languages_id_seq", sequenceName = "languages_id_seq")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "language")
    private List<Book> books;


}
