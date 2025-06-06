package com.martin.bookstore.book.persistence.repository;

import com.martin.bookstore.book.persistence.entity.BookGenre;
import com.martin.bookstore.book.persistence.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
    void deleteAllByBook(Book book);

    @Query("""
        select bg.book
        from BookGenre bg
        where bg.genre.id = :genreId
""")
    Page<Book> findBooksByGenreId(@Param("genreId") Long genreId, Pageable pageable);
}
