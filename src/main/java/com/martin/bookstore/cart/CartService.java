package com.martin.bookstore.cart;

import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.User;
import com.martin.bookstore.exception.BookMissingPriceException;
import com.martin.bookstore.exception.InsufficientStockException;
import com.martin.bookstore.exception.NotFoundException;
import com.martin.bookstore.repository.BookRepository;
import com.martin.bookstore.repository.UserRepository;
import com.martin.bookstore.security.config.CustomUserDetails;
import com.martin.bookstore.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final StockService stockService;

    @Transactional
    public CartFullResponseDto addToCart(CartRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        if (book.getPrice() == null) {
            throw new BookMissingPriceException("Book with ID " + book.getId() + " has no price and cannot be added to the cart.");
        }

        if (!stockService.reserveStock(book, requestDto.getQuantity())) {
            throw new InsufficientStockException("Not enough stock available for book: " + book.getTitle());
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + requestDto.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(requestDto.getQuantity());
            cart.getItems().add(newItem);
        }

        cart.setLastModified(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.toCartFullResponseDto(cart);
    }

    @Transactional
    public CartFullResponseDto getUserCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return cartMapper.toCartFullResponseDto(cart);
    }

    @Transactional
    public void removeFromCart(Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart not found for user"));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Book not found in cart"));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);
    }
}
