package com.martin.bookstore.cart;

import com.martin.bookstore.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CartExpirationCleaner implements CommandLineRunner {

    private final CartRepository cartRepository;
    private final StockService stockService;
    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        applicationContext.getBean(CartExpirationCleaner.class).cleanExpiredCarts();
    }

    @Scheduled(fixedRate = 3600000) // 1 hour
    public void scheduleCleanup() {
        applicationContext.getBean(CartExpirationCleaner.class).cleanExpiredCarts();
    }

    @Transactional
    public void cleanExpiredCarts() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        cartRepository.findAll().stream()
                .filter(cart -> cart.getLastModified().isBefore(oneHourAgo))
                .forEach(cart -> {
                    cart.getItems().forEach(item -> stockService.releaseReserved(item.getBook(), item.getQuantity()));
                    cart.getItems().clear();
                    cartRepository.delete(cart);
                });
    }
}
