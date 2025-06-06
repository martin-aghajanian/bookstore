package com.martin.bookstore.cart.persistence.repository;

import com.martin.bookstore.cart.persistence.entity.Cart;
import com.martin.bookstore.user.persistence.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    List<Cart> findByLastModifiedBefore(LocalDateTime time);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select c
        from Cart c
        where c.lastModified < :threshold
""")
    List<Cart> findExpiredCarts(@Param("threshold") LocalDateTime threshold);
}
