package com.martin.bookstore.models.book.persistence.repository;

import com.martin.bookstore.models.book.persistence.entity.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
}
