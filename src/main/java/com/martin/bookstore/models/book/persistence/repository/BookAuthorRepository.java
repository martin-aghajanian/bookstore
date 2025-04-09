package com.martin.bookstore.models.book.persistence.repository;

import com.martin.bookstore.models.book.persistence.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
}
