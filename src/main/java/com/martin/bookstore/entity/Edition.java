package com.martin.bookstore.entity;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "editions_id_seq")
    @SequenceGenerator(name = "editions_id_seq", sequenceName = "editions_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "edition")
    private List<Book> books;
}
