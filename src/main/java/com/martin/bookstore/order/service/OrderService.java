package com.martin.bookstore.order.service;

import com.martin.bookstore.cart.persistence.entity.Cart;
import com.martin.bookstore.cart.persistence.entity.CartItem;
import com.martin.bookstore.cart.persistence.repository.CartRepository;
import com.martin.bookstore.order.criteria.OrderSearchCriteria;
import com.martin.bookstore.order.dto.OrderItemRequestDto;
import com.martin.bookstore.order.dto.OrderRequestDto;
import com.martin.bookstore.order.dto.OrderResponseDto;
import com.martin.bookstore.order.enums.OrderStatus;
import com.martin.bookstore.order.persistence.entity.Order;
import com.martin.bookstore.order.persistence.entity.OrderItem;
import com.martin.bookstore.order.persistence.repository.OrderRepository;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.shared.exception.*;
import com.martin.bookstore.user.persistence.entity.User;
import com.martin.bookstore.security.config.CustomUserDetails;
import com.martin.bookstore.stock.persistence.entity.Stock;
import com.martin.bookstore.stock.persistence.repository.StockRepository;
import com.martin.bookstore.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;

    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Cart cart = cartRepository.findByUser(user).orElseThrow(() ->
                new NotFoundException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemRequestDto selected : dto.getItems()) {
            CartItem cartItem = cart.getItems().stream()
                    .filter(ci -> ci.getBook().getId().equals(selected.getBookId()))
                    .findFirst()
                    .orElseThrow(() -> new BookNotInCartException("Selected book not found in cart: " + selected.getBookId()));

            if (!selected.getQuantity().equals(cartItem.getQuantity())) {
                throw new InvalidOrderQuantityException("Mismatch in quantity for book ID: " + selected.getBookId());
            }

            Book book = cartItem.getBook();
            Stock stock = stockRepository.findByBookId(book.getId());
            if (stock == null || stock.getQuantityReserved() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Insufficient reserved stock for book: " + book.getTitle());
            }

            stock.setQuantityReserved(stock.getQuantityReserved() - cartItem.getQuantity());
            stock.setQuantitySold(stock.getQuantitySold() + cartItem.getQuantity());

            OrderItem orderItem = OrderMapper.toOrderItem(book, cartItem.getQuantity());
            orderItem.setOrder(null);
            orderItems.add(orderItem);

            totalPrice += orderItem.getPriceAtPurchase() * orderItem.getQuantity();
        }

        Order order = OrderMapper.toEntity(dto, user, totalPrice);
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            order.getOrderItems().add(item);
        }

        Order savedOrder = orderRepository.save(order);

        dto.getItems().forEach(itemDto ->
                cart.getItems().removeIf(ci -> ci.getBook().getId().equals(itemDto.getBookId()))
        );

        return OrderMapper.toDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<OrderResponseDto> getUserOrders(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findByUser(user, pageable);

        Page<OrderResponseDto> mapped = orders.map(OrderMapper::toDto);
        return PageResponseDto.from(mapped);
    }

    @Transactional
    public void payForOrder(Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderAccessDeniedException("You can only pay for your own orders.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderPaymentNotAllowedException("Order is not in a payable state.");
        }

        // payment simulation
        mockCardPayment(order);

        for (OrderItem item : order.getOrderItems()) {
            stockService.finalizePurchase(item.getBook(), item.getQuantity());
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    private void mockCardPayment(Order order) {
        // pretend this sends the card number to a bank api
        System.out.println("simulating card payment for order " + order.getId());
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderAccessDeniedException("You can only cancel your own orders.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderCancellationNotAllowedException("Only pending orders can be cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);

        for (OrderItem item : order.getOrderItems()) {
            stockService.releaseReserved(item.getBook(), item.getQuantity());
        }

        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<OrderResponseDto> getAllOrders(OrderSearchCriteria criteria) {
        Page<Order> orders = orderRepository.findAll(criteria, criteria.buildPageRequest());
        Page<OrderResponseDto> mapped = orders.map(OrderMapper::toDto);
        return PageResponseDto.from(mapped);
    }
}
