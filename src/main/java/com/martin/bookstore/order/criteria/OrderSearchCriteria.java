package com.martin.bookstore.order.criteria;

import com.martin.bookstore.order.enums.OrderStatus;
import com.martin.bookstore.shared.criteria.SearchCriteria;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
public class OrderSearchCriteria extends SearchCriteria {
    private String userQuery;
    private OrderStatus status;// PAID, PENDING, CANCELLED
    private LocalDate minDate;
    private LocalDate maxDate;
    private String sortBy = "createdAt";
    private String direction = "ASC";

    @Override
    public PageRequest buildPageRequest() {
        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);
        return super.buildPageRequest().withSort(Sort.by(sortDirection, sortBy));
    }
}
