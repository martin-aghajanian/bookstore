package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.BookCharacter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
    void deleteAllByBook(Book book);

    @Query("""
        select bc.book
        from BookCharacter bc
        where bc.character.id = :awardId
    """)
    Page<Book> findBooksByCharacterId(@Param("awardId") Long awardId, Pageable pageable);
}
