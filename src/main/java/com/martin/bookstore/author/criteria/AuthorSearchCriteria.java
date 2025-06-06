package com.martin.bookstore.author.criteria;


import com.martin.bookstore.shared.criteria.SearchCriteria;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class AuthorSearchCriteria extends SearchCriteria {

    private String fullName;
    private Boolean goodReadsAuthor;

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();
        return pageRequest.withSort(Sort.by("fullName").ascending());
    }
}
