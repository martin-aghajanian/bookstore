package com.martin.bookstore.entity;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settings_id_seq")
    @SequenceGenerator(name = "settings_id_seq", sequenceName = "settings_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "setting")
    private List<BookSetting> bookSetting;
}
