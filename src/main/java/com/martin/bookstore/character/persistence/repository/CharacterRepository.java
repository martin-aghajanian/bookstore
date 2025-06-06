package com.martin.bookstore.character.persistence.repository;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.character.persistence.entity.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<com.martin.bookstore.character.persistence.entity.Character> findByName(String name);

    @Query("""
        SELECT bc.book FROM BookCharacter bc
        WHERE bc.character.id = :characterId
    """)
    Page<Book> findBooksByCharacterId(@Param("characterId") Long characterId, Pageable pageable);
}
