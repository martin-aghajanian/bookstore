package com.martin.bookstore.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @NotEmpty
    private List<OrderItemRequestDto> items;

    @NotNull
    private String paymentMethod;

    @NotNull
    private String shippingAddress;
}
