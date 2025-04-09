package com.martin.bookstore.models.format.persistence.entity;

import com.martin.bookstore.models.book.persistence.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "book_formats")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BookFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "format", unique = true, nullable = false)
    private String format;

    @OneToMany(mappedBy = "bookFormat")
    private List<Book> books;
}
