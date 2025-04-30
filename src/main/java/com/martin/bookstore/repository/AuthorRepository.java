package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByFullName(String authorName);

    List<Author> findByFullNameContainingIgnoreCase(String fullName);

    List<Author> findByGoodReadsAuthor(boolean goodReadsAuthor);

    @Query("""
        SELECT DISTINCT a FROM Author a
        JOIN a.bookAuthor ba
        WHERE (:goodreads IS NULL OR a.goodReadsAuthor = :goodreads)
        AND (:name = '' OR (LOWER(a.fullName) LIKE LOWER(CONCAT('%', :name, '%'))))
        AND (:contribution IS NULL OR LOWER(ba.contribution) LIKE LOWER(CONCAT('%', :contribution, '%')))
    """)
    Page<Author> filterAuthors(@Param("goodreads") Boolean goodreads,
                               @Param("contribution") String contribution,
                               Pageable pageable);

    Page<Author> findByFullNameContainingIgnoreCase(String name, Pageable pageable);
}
