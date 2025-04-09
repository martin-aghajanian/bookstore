package com.martin.bookstore.models.genre.persistence.entity;

import com.martin.bookstore.models.book.persistence.entity.BookGenre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "genres")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<BookGenre> bookGenres;
}
