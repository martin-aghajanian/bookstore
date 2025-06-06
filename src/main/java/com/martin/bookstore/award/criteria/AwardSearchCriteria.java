package com.martin.bookstore.award.criteria;

import com.martin.bookstore.shared.criteria.SearchCriteria;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
public class AwardSearchCriteria extends SearchCriteria {
    private String name;
    private LocalDate year;

    @Override
    public PageRequest buildPageRequest() {
        return super.buildPageRequest()
                .withSort(Sort.by("name").ascending());
    }
}
