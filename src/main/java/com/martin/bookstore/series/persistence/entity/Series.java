package com.martin.bookstore.series.persistence.entity;

import com.martin.bookstore.book.persistence.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "series")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_id_seq")
    @SequenceGenerator(name = "series_id_seq", sequenceName = "series_id_seq")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "series")
    private List<Book> books;
}
