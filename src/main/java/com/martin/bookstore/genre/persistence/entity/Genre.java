package com.martin.bookstore.genre.persistence.entity;

import com.martin.bookstore.book.persistence.entity.BookGenre;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genres_id_seq")
    @SequenceGenerator(name = "genres_id_seq", sequenceName = "genres_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<BookGenre> bookGenres;
}
