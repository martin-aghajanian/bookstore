package com.martin.bookstore.models.book.persistence.repository;

import com.martin.bookstore.models.book.persistence.entity.BookCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
}
