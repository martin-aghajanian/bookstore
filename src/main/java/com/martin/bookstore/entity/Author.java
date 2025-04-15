package com.martin.bookstore.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@RequiredArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authors_id_seq")
    @SequenceGenerator(name = "authors_id_seq", sequenceName = "authors_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "goodread_author", nullable = false)
    private Boolean goodReadsAuthor;

    @OneToMany(mappedBy = "author")
    private List<BookAuthor> bookAuthor;
}
