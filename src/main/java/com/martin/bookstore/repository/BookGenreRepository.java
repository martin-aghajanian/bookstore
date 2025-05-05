package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.BookGenre;
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
