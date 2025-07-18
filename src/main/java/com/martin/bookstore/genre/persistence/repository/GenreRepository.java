package com.martin.bookstore.genre.persistence.repository;

import com.martin.bookstore.genre.persistence.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String genreName);
}
