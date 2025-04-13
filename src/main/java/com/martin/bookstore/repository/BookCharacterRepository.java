package com.martin.bookstore.repository;

import com.martin.bookstore.entity.BookCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
}
