package com.martin.bookstore.persistence.repository;

import com.martin.bookstore.persistence.entity.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
}
