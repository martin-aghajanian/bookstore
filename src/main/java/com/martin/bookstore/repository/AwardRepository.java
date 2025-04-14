package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Award;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    Optional<Award> findByName(String name);

    @Query(value = """
    SELECT DISTINCT a.*
    FROM awards a
    JOIN books_awards ba ON a.id = ba.award_id
    WHERE EXTRACT(YEAR FROM ba.year) = :year
    """,
            countQuery = """
    SELECT COUNT(DISTINCT a.id)
    FROM awards a
    JOIN books_awards ba ON a.id = ba.award_id
    WHERE EXTRACT(YEAR FROM ba.year) = :year
    """,
            nativeQuery = true)
    Page<Award> findAwardsByYear(@Param("year") int year, Pageable pageable);

    Page<Award> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
