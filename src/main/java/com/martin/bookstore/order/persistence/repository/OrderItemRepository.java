package com.martin.bookstore.order.persistence.repository;

import com.martin.bookstore.order.persistence.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
