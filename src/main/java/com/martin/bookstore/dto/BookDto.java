package com.martin.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private Long id;
    private String name;


    public BookDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
