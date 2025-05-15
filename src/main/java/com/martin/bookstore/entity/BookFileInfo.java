package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books_file_info")
@Getter
@Setter
@NoArgsConstructor
public class BookFileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_file_info_id_seq")
    @SequenceGenerator(name = "books_file_info_id_seq", sequenceName = "books_file_info_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_info_id", nullable = false)
    private FileInfo fileInfo;
}
