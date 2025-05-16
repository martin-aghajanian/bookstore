package com.martin.bookstore.security.user;

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

}
