package com.martin.bookstore.cart.clrunner;

import com.martin.bookstore.cart.persistence.entity.Cart;
import com.martin.bookstore.cart.persistence.repository.CartRepository;
import com.martin.bookstore.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        List<Cart> expiredCarts = cartRepository.findExpiredCarts(oneHourAgo);

        for (Cart cart : expiredCarts) {
            cart.getItems().forEach(item ->
                    stockService.releaseReserved(item.getBook(), item.getQuantity())
            );
            cart.getItems().clear();
        }
    }
}
