package com.martin.bookstore.persistence.repository;

import com.martin.bookstore.persistence.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    Optional<Award> findByName(String name);
}
