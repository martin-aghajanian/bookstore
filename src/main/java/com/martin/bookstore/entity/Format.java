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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "formats_id_seq")
    @SequenceGenerator(name = "formats_id_seq", sequenceName = "formats_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "format", unique = true, nullable = false)
    private String format;

    @OneToMany(mappedBy = "format")
    private List<Book> books;
}
