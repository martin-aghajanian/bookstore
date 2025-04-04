package com.martin.bookstore.persistence.repository;

import com.martin.bookstore.persistence.entity.BookFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookFormatRepository extends JpaRepository<BookFormat, Long> {
    Optional<BookFormat> findByFormat(String name);
}
