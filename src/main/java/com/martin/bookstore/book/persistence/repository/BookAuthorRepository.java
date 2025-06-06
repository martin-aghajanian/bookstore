package com.martin.bookstore.book.persistence.repository;

import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.entity.BookAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    void deleteAllByBook(Book book);

    @Query("""
        select ba.book
        from BookAuthor ba
        where ba.author.id = :authorId
    """)
    Page<Book> findBooksByAuthorId(@Param("authorId") Long authorId, Pageable pageable);
}
