package com.martin.bookstore.wishlist.persistence.repository;

import com.martin.bookstore.user.persistence.entity.User;
import com.martin.bookstore.wishlist.persistence.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUser(User user);

    boolean existsByUser(User user);
}
