package com.martin.bookstore.models.award.persistence.entity;

import com.martin.bookstore.models.book.persistence.entity.BookAward;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "awards")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "award")
    private List<BookAward> bookAwards;
}
