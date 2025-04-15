package com.martin.bookstore.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "publishers")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publishers_id_seq")
    @SequenceGenerator(name = "publishers_id_seq", sequenceName = "publishers_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "publisher")
    private List<Book> books;
}
