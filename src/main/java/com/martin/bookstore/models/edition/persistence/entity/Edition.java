package com.martin.bookstore.models.edition.persistence.entity;

import com.martin.bookstore.models.book.persistence.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "editions")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Edition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "edition")
    private List<Book> books;
}
