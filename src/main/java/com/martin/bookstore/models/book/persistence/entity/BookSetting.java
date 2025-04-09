package com.martin.bookstore.models.book.persistence.entity;

import com.martin.bookstore.models.setting.persistence.entity.Setting;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "books_settings")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BookSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;

}
