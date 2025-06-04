package com.martin.bookstore.order;

import com.martin.bookstore.dto.response.PageResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto placeOrder(@Valid @RequestBody OrderRequestDto requestDto) {
        return orderService.placeOrder(requestDto);
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping
    public PageResponseDto<OrderResponseDto> getUserOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.getUserOrders(page, size);
    }

    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAuthority('user:update')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void payForOrder(@PathVariable Long id) {
        orderService.payForOrder(id);
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/all")
    public PageResponseDto<OrderResponseDto> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.getAllOrders(page, size);
    }
}
