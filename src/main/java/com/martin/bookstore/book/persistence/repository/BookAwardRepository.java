package com.martin.bookstore.book.persistence.repository;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.entity.BookAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
    void deleteAllByBook(Book book);

    @Query("""
        select ba.book
        from BookAward ba
        where ba.award.id = :awardId
    """)
    Page<Book> findBooksByAwardId(@Param("awardId") Long awardId, Pageable pageable);
}
