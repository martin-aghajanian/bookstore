package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByName(String name);
}
