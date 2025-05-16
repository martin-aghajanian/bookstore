package com.martin.bookstore.repository;

import com.martin.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
        select u from User u
        left join fetch u.role r
        left join fetch r.permissions
        where u.username = :username
    """)
    Optional<User> findByUsernameWithRoleAndPermissions(@Param("username") String username);

    @Query("""
        select u
        from User u
        join fetch u.role
        where u.id = :id
    """)
    Optional<User> findByIdWithRole(Long id);
}
