package com.martin.bookstore.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class UserSearchCriteria extends SearchCriteria {

    private String username;
    private String email;
    private String role;
    private String sortBy = "username";
    private String direction = "ASC";

    @Override
    public PageRequest buildPageRequest() {
        return super.buildPageRequest()
                .withSort(Sort.by(sortBy).ascending());
    }

}
