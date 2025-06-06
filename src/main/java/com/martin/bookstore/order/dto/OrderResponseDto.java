package com.martin.bookstore.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    private Long orderId;
    private List<OrderItemResponseDto> items;
    private Double totalPrice;
    private String status;
    private String paymentMethod;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private String username;
}
