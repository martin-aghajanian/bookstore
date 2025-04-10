package com.martin.bookstore.models.author.persistence.repository;

import com.martin.bookstore.models.author.persistence.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByFullName(String authorName);

    List<Author> findByFullNameContainingIgnoreCase(String fullName);

    List<Author> findByGoodReadsAuthor(boolean goodReadsAuthor);

    Page<Author> findAll(Pageable pageable);

}
