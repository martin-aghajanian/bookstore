package com.martin.bookstore.shared.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Setter
@Getter
public class SearchCriteria {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private int page;
    private int size;
    private String sortBy = "id";
    private String direction = "ASC";

    public PageRequest buildPageRequest() {
        int pageNumber = Math.max(page, 0);
        int pageSize = size <= 0 ? DEFAULT_PAGE_SIZE : size;

        return PageRequest.of(pageNumber, pageSize);
    }
}
