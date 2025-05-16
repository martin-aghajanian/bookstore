package com.martin.bookstore.repository;

import com.martin.bookstore.criteria.UserSearchCriteria;
import com.martin.bookstore.entity.User;
import com.martin.bookstore.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        select new com.martin.bookstore.security.user.UserResponseDto(
                    u.id,
                    u.username,
                    u.email,
                    u.role
                )
        from User u
        where (:#{#criteria.username} is null or lower(u.username) like concat('%', lower(:#{#criteria.username}), '%'))
        and (:#{#criteria.email} is null or lower(u.email) like concat('%', lower(:#{#criteria.email}), '%'))
        and (:#{#criteria.role} is null or lower(u.role) = lower(:#{#criteria.role}))
    """)
    Page<UserResponseDto> findAll(UserSearchCriteria criteria, Pageable pageable);
}
