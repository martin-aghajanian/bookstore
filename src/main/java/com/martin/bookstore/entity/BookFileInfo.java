package com.martin.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books_file_info")
@Getter
@Setter
@RequiredArgsConstructor
public class BookFileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_file_info_id_seq")
    @SequenceGenerator(name = "books_file_info_id_seq", sequenceName = "books_file_info_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;
}
