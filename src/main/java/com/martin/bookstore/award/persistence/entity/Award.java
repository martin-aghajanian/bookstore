package com.martin.bookstore.award.persistence.entity;

import com.martin.bookstore.book.persistence.entity.BookAward;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "awards_id_seq")
    @SequenceGenerator(name = "awards_id_seq", sequenceName = "awards_id_seq")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "award")
    private List<BookAward> bookAwards;
}
