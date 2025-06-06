package com.martin.bookstore.format.persistence.repository;

import com.martin.bookstore.format.persistence.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormatRepository extends JpaRepository<Format, Long> {
    Optional<Format> findByFormat(String name);
}
