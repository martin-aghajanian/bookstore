package com.martin.bookstore.order.service;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.order.dto.OrderItemResponseDto;
import com.martin.bookstore.order.dto.OrderRequestDto;
import com.martin.bookstore.order.dto.OrderResponseDto;
import com.martin.bookstore.order.enums.OrderStatus;
import com.martin.bookstore.order.persistence.entity.Order;
import com.martin.bookstore.order.persistence.entity.OrderItem;
import com.martin.bookstore.user.persistence.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderRequestDto dto, User user, double totalPrice) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(new ArrayList<>());
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setShippingAddress(dto.getShippingAddress());
        return order;
    }

    public static OrderItem toOrderItem(Book book, int quantity) {
        OrderItem item = new OrderItem();
        item.setBook(book);
        item.setQuantity(quantity);
        item.setPriceAtPurchase(book.getPrice());
        return item;
    }

    public static OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setStatus(order.getStatus().name());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setUsername(order.getUser().getUsername());

        List<OrderItemResponseDto> itemDtos = order.getOrderItems().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    public static OrderItemResponseDto toDto(OrderItem item) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setBookId(item.getBook().getId());
        dto.setBookTitle(item.getBook().getTitle());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtPurchase(item.getPriceAtPurchase());
        return dto;
    }
}
