package com.martin.bookstore.dto.filters;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class BookFilterRequestDto {

    private Long genreId;
    private Long languageId;
    private Long formatId;

    private Integer minPages;
    private Integer maxPages;

    private Double minPrice;
    private Double maxPrice;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate minDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate maxDate;

    private String sortBy = "publishDate";
    private String direction = "desc";

}
