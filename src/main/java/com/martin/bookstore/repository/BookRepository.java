package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByIsbn(Long isbn);

    Optional<Book> findByTitle(String title);

    Page<Book> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);

    Page<Book> findByEditionId(Long editionId, Pageable pageable);

    Page<Book> findByFormatId(Long formatId, Pageable pageable);

    @Query("""
        SELECT bg.book FROM BookGenre bg
        WHERE bg.genre.id = :genreId
    """)
    Page<Book> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);

    Page<Book> findByLanguageId(Long languageId, Pageable pageable);

    Page<Book> findBySeriesId(Long seriesId, Pageable pageable);

    @Query("""
        SELECT bs.book FROM BookSetting bs
        WHERE bs.setting.id = :settingId
    """)
    Page<Book> findBySettingId(@Param("settingId") Long settingId, Pageable pageable);

}
