package com.martin.bookstore.models.setting.persistence.entity;

import com.martin.bookstore.models.book.persistence.entity.BookSetting;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "settings")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "setting")
    private List<BookSetting> bookSetting;
}
