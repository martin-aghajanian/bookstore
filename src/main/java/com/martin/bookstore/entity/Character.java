package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "characters")
@Getter
@Setter
@RequiredArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "characters_id_seq")
    @SequenceGenerator(name = "characters_id_seq", sequenceName = "characters_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "character")
    private List<BookCharacter> bookCharacters;
}
