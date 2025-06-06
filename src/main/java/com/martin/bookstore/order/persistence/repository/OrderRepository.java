package com.martin.bookstore.order.persistence.repository;

import com.martin.bookstore.order.criteria.OrderSearchCriteria;
import com.martin.bookstore.order.persistence.entity.Order;
import com.martin.bookstore.user.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);

    @Query("""
    select distinct o from Order o
    join o.user u
    where (:#{#criteria.userQuery} is null
        or lower(u.username) like lower(concat('%', :#{#criteria.userQuery}, '%'))
        or lower(u.email) like lower(concat('%', :#{#criteria.userQuery}, '%')))
    and (:#{#criteria.status} is null or o.status = :#{#criteria.status})
    and o.createdAt >= coalesce(:#{#criteria.minDate}, o.createdAt)
    and o.createdAt <= coalesce(:#{#criteria.maxDate}, o.createdAt)
""")
    Page<Order> findAll(OrderSearchCriteria criteria, Pageable pageable);
}
