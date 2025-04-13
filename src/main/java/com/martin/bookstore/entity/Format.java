package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "formats")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Format {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "format", unique = true, nullable = false)
    private String format;

    @OneToMany(mappedBy = "format")
    private List<Book> books;
}
