package com.martin.bookstore.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
public class BookSearchCriteria extends SearchCriteria{
    private String query;
    private Long authorId;
    private Long genreId;
    private Long languageId;
    private Long formatId;
    private Long seriesId;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Integer minPages;
    private Integer maxPages;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy = "title";
    private String direction = "ASC";

    @Override
    public PageRequest buildPageRequest() {
        return super.buildPageRequest()
                .withSort(Sort.by(sortBy).ascending());
    }
}