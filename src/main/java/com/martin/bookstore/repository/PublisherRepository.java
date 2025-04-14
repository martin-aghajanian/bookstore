package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);

    Page<Publisher> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
